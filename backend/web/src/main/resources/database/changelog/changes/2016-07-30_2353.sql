--liquibase formatted sql
--changeset tomas:add_person_balance_view_table splitStatements:false

CREATE OR REPLACE VIEW person_balance
AS 
	SELECT debt_calculation.person_id, debt_calculation.debt_total, credit_calculation.credit_total
	FROM
		(
			select per.id as "person_id",
			SUM((p.unit_price - (p.unit_price*sl.discount)) * sl.quantity) as "debt_total"
			from sale s, saleline sl, batch b, product p, person per
			where s.paied_out = false -- facturas no pagadas
			and b.product = p.id
			and per.id = s.person
			group by per.id
		) as "debt_calculation"
	LEFT OUTER JOIN
		(
			select per.id as "person_id",
			SUM(settl.amount) as "credit_total"
			from settlement settl, person per
			where settl.discounted = false
			and per.id = settl.person
			group by per.id
		) as "credit_calculation"
	ON credit_calculation.person_id = debt_calculation.person_id;