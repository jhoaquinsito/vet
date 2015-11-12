# cargo los productos provistos por Miriam
products <- read.csv("/home/lautaro/Documentos/genesis/migracion/datos/transformados/products.csv",
                      sep=";")

# extraigo los fabricantes de los productos sin repeticiones
manus_miriam <- as.vector(unique(products$RUBRO))
manus_miriam <- sort(manus_miriam)
manus_miriam <- sapply(manus_miriam, function (x) sub("\\s+$", "", x))
manus_miriam <- sapply(manus_miriam, function (x) sub("^\\s+", "", x))

# cargo los fabricantes estandarizados
manus_stand <- read.csv("/home/lautaro/Documentos/genesis/migracion/datos/transformados/manufacturers.csv")
manus_stand <- as.vector(manus_stand$manufacturer)
manus_stand <- sapply(manus_stand, function (x) sub("\\s+$", "", x))
manus_stand <- sapply(manus_stand, function (x) sub("^\\s+", "", x))
manus_stand <- sort(manus_stand)

# calculo la distancia de levenstein entre los fabricantes de los productos y los estandarizados
distances <- c()
for(manu in manus_stand){
    row<-sapply(manus_miriam, adist, x=manu, ignore.case=TRUE)
    distances <- rbind(distances, row)
}

###########################################################

# formo pares entre los fabricantes de los productos y los mas cercanos de los fabricantes
# estandar de cada uno
map<-c()
for(i in 1:length(manus_miriam)){
    manu_stand <- manus_stand[distances[,i]==min(distances[,i])]
    map<-rbind(map, c(manus_miriam[i], manu_stand[1]))
}

# trato algunos casos particulares
map[map[,1]=="LTF",2]<-"L.T. FRANCES"

map[map[,1]=="VON FRANKEN",2]<-"VON FRANKEN FATRO"

map[map[,1]=="von franken",2]<-"VON FRANKEN FATRO"

map[map[,1]=="FATRO",2]<-"VON FRANKEN FATRO"

map[map[,1]=="",2]<-"No Especificado"

map[map[,1]=="OTROS",2]<-"No Especificado"


# asigno nombres a las columnas del data frame
map <- as.data.frame(map)
names(map)<-c("raw", "standar")


# guardo los resultados en un csv
write.csv(map, "/home/lautaro/Documentos/genesis/migracion/datos/transformados/manufacturers_map.csv")