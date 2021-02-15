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
