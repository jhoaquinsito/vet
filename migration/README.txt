#######################################
###
###		CONTENIDO 
###
#######################################

1. ETLs: con tiene las trnasformaciones y el trabajo que componen el proceso de migración.

2. resourses: contiene los archivos
	- manufacturers.csv: con los fabricantes estandarizados.
	- manufacturers_map.csv: con la correspondecia entre fabricante y fabricante estandarizado.
	- presentations.csv: con las presentaciones estandarizadas.
	- presentations_map.csv: con la correspondencia entre presentación y presentación estandarizada.
	- products.csv: con los productos del sistema viejo.

3. Scripts_R: con 2 scripts en lenguaje R
	- similitude_manufacturers.R: para crear las correspondecia entre fabricantes y fabricante estandarizados.
	- similitude_presentations.R: para crear las correspondencia entre presentaciones y presentaciones estandarizadas.


#######################################
###
###	     REQUERIMIENTOS 
###
#######################################

1. Pentaho Data Integration

2. Lenguaje R


#######################################
###
###	CONSIDERACIONES 
###
#######################################

1. Antes de ejecutar se debe indicar el la transformación stage_products 
	- El directorio local donde se ubica el archivo DBF con los productos del sistema viejo.
	- El directorio local donde se van a ubicar los archivos con datos erroneos.
	- El directorio local donde se van a ubicar los archivos con los priductos extraídos (área de staging).

