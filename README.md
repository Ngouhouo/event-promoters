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


![filterEventsFromPromoterId](https://user-images.githubusercontent.com/74736395/107986612-b83dc700-6fcc-11eb-8fb3-d1da7b6d20a9.png)
![filterEventsFromPromoterId](https://user-images.githubusercontent.com/74736395/107986612-b83dc700-6fcc-11eb-8fb3-d1da7b6d20a9.png)
![filterStatesStats](https://user-images.githubusercontent.com/74736395/107986628-bffd6b80-6fcc-11eb-82b5-a47bd9723c14.png)
![filterStatisticsByPromoterId](https://user-images.githubusercontent.com/74736395/107986635-c5f34c80-6fcc-11eb-9172-f3f161d2d564.png)
![filterStatsByEventGenre](https://user-images.githubusercontent.com/74736395/107986651-cd1a5a80-6fcc-11eb-8470-a36336badbfe.png)
![newZealandEventsInTimeRange](https://user-images.githubusercontent.com/74736395/107986659-d4d9ff00-6fcc-11eb-9c8b-f9b0b46b6545.png)
![totalEventsInTimeRange](https://user-images.githubusercontent.com/74736395/107986672-dc99a380-6fcc-11eb-94c2-c60c51fd1d17.png)
