# cargo los productos provistos por Miriam
productos <- read.csv("/home/lautaro/Documentos/genesis/migracion/datos/transformados/products.csv",
                      sep=";")

# extraigo las presentaciones de los productos sin repeticiones
pres_miriam <- as.vector(unique(productos$PRESENTAC))
pres_miriam <- sort(pres_miriam)

# cargo las presentaciones estandarizados
pres_stand <- c("AEROSOL", "EMULSIÓN", "GOTAS", "GRANULADO", "INYECTABLE", "ORAL", "PASTA",
              "PASTILLAS", "POLVO", "POMADA", "SOLUCIÓN", "SPRAY")

# calculo la distancia de levenstein entre las presentaciones de los productos y los estandarizados
distances <- c()
for(lab in pres_stand){
    row<-sapply(pres_miriam, adist, x=lab, ignore.case=TRUE)
    distances <- rbind(distances, row)
}

###########################################################

# formo pares entre las presentaciones de los productos y las mas cercanas de las presentaciones
# estandar de cada una
map<-c()
for(i in 1:length(pres_miriam)){
    pres <- pres_stand[distances[,i]==min(distances[,i])]
    map<-rbind(map, c(pres_miriam[i], pres[1]))
}

# trato algunos casos particulares
map[map[,1]=="GRANOS",2]<-"GRANULADO"

map[map[,1]=="ML",2]<-"No Especificado"

map[map[,1]=="",2]<-"No Especificado"

map[grepl("\\d", map[,1], perl=TRUE), 2]<-"No Especificado"


# asigno nombres a las columnas del data frame
map <- as.data.frame(map)
names(map)<-c("raw", "standar")


# guardo los resultados en un csv
write.csv(map, "/home/lautaro/Documentos/genesis/migracion/datos/transformados/presentation_map.csv")