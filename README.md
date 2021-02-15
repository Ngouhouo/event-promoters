# EVENTS-PROMOTERS          ![Capture](https://user-images.githubusercontent.com/74736550/107882529-265d8d80-6eea-11eb-8ce2-3a3a07161875.PNG)
### EVENTS-PROMOTERS

**Events-Promoters è un programma che permette di studiare i promoter degli eventi che avranno luogo in Australia e Nuova Zelanda,con questo programma l'utente finale ha la possibilità di visualizzare delle statistiche per ogni stato, visualizzare statistiche di uno o più promoter scelti dall'utente (in base all'ID del promoter).Inoltre l'utente potrà avere la possibilità di calcolare/filtrare le statistiche in base a dei filtri scelti dall'utente stesso.Inoltre nell'applicazione è stata predefinita una lista di Stati alla disposizione dell'utente come referimento.abbiamo utilizzato il software Postman per effettuare le varie operazioni sopra citate tramite la porta "localhost:8080". Di seguito sono elencate le rotte che devono essere inserite dopo la porta appena menzionata, per eseguire le relative chiamate. (Il codice del progetto è interamente in java e si trova dentro "src".)**

| Syntax      | Description | INPUT                      |   DESCRIPTION           |    
| ----------- | ----------- |  --------------------------| ------------------------|                        
| GET         |  /stats/states           |                            | ritorna le statististiche di ogni rotta                        |
| GET         | /event/{id}       |     id event                       | return event per ID                        |
| GET        |/events/Australia/period     |      data inizio e fine                       | ritorna gli promoter ed eventi dell  australia chiedendo tramite una Hmap la data dell inizio e la data della fine                         |
| GET       |   /events/New_Zealand/period          |   data inizio e fine                         |      ritorna gli promoter ed eventi dell  australia chiedendo tramite una Hmap la data dell inizio e la data                   |
|GET          | /events/period        |      data inizio e fine                       |   ritorna gli promoter ed eventi dell  australia chiedendo tramite una Hmap la data dell inizio e la data|  
| GET            |          /stats/states/{state_name}             |    nome dell stato                          |      ritorna stat                      |                        
|  GET           |    /events/promoter/{promoterId}                   |     id stato                         |            ritorna filtri di un promoter                   |
|  GET           |        /stats/promoter/{promoterId}               |       id promoter                       |                 ritorna statistiche di un promoter                |
|  GET           |      /events/genre/{segment_name}                 |       segment name                       |                     ritorna gli eventi per segment per genere           |
 ### USE CASE DIAGRAMM :
![use case promoter](https://user-images.githubusercontent.com/74736550/107983935-1cf62300-6fc7-11eb-9bb2-597e6071217d.PNG)


 ### DIAGRAMMA DELLE CLASSI:
![ProjectClassDiagram](https://user-images.githubusercontent.com/74736395/107986104-a90a4980-6fcb-11eb-81ba-28850704aa61.png)


### DIAGRAMMA DELLE CLASSI:
![australiaEventsInTimeRange](https://user-images.githubusercontent.com/74736395/107986412-4bc2c800-6fcc-11eb-9cb2-ccba72c7031d.png)
![eventById](https://user-images.githubusercontent.com/74736395/107986492-73b22b80-6fcc-11eb-9cda-9a5e9068e913.png)
