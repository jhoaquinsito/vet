--liquibase formatted sql
--changeset tomas:update_person_balance_view_table splitStatements:false

BEGIN TRANSACTION;

-- elimino vista que utiliza tabla settlements
DROP VIEW public.person_balance;

-- elimino constraint default de columna discounted porque era de tipo boolean
ALTER TABLE settlement ALTER discounted DROP DEFAULT;

-- cambio la columna de discounted: boolean a numeric
ALTER TABLE settlement ALTER discounted TYPE NUMERIC(17,4) USING CASE WHEN discounted=TRUE THEN amount ELSE 0 END;

-- agrego constraint default de columna discounted de tipo numeric
ALTER TABLE settlement ALTER discounted SET DEFAULT 0;
  
-- agrego vista actualizada para funcionar con el nuevo tipo de columna
CREATE OR REPLACE VIEW public.person_balance AS 
 SELECT debt_calculation.person_id,
    debt_calculation.debt_total,
    credit_calculation.credit_total
   FROM ( SELECT per.id AS person_id,
            sum((p.unit_price - p.unit_price * sl.discount / 100)::double precision * sl.quantity) AS debt_total
           FROM sale s,
            saleline sl,
            batch b,
            product p,
            person per
          WHERE s.paied_out = false AND b.product = p.id AND per.id = s.person AND sl.sale_id = s.id AND b.id = sl.batch
          GROUP BY per.id) debt_calculation
     LEFT JOIN ( SELECT per.id AS person_id,
            sum(settl.amount - settl.discounted) AS credit_total
           FROM settlement settl,
            person per
          WHERE settl.discounted < settl.amount AND per.id = settl.person
          GROUP BY per.id) credit_calculation ON credit_calculation.person_id = debt_calculation.person_id;

COMMIT;