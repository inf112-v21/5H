# Oppgave 1


### Gruppenavn: 5H 

## Gruppemedlemmers kode kompetanse og roller:
* [Kodeansvarlig - java] **Skjalg Eide Hodneland**: Java, Python, Haskell, JavaScript, SQL, CSS, HTML
* [Kodeansvarlig - Libgdx] **Håkon**: Java, Python, Haskell, SQL
* [Teamlead] **Jonas**: Python, litt Java, datastrukturer, databaser
* [Testansvarlig] **Emil**: Python, Java, Haskell, SQL, datastrukturer
* [Spesifikasjonsansvarlig] **Hans Albert**: Python, Haskell
* [Kundeansvarlig /kontakt] **Henrik**: Python, litt Haskell, SQL, HTML

### Oppgaver for roller:
* Kodeansvarlig -Java:
  *  Hovedansvar for alt med Java koden, skal ha overordnet ansvar for å hjelpe med problemer i javakoden. Ansvarlig for at java koden følger best-practices.
* Kodeansvarlig - Libgdx:
  * Hovedansvar for alt med libgdx. Dette innebærer å sette seg inn i hvordan å best bruke dette library til å gjennomføre prosjektet.
* Teamlead: 
  * Delegere oppgaver slik at det er oppgaver for alt som skal leveres. Ansvarlig for at alt som skal inn til en innlevering blir delegert og utført. Ansvarlig for at vi leverer inn alt som skal til en innlevering.
* Testansvarlig : 
  * Hovedansvar for testene, og å hjelpe til med å skrive testene. Ansvarlig for at java-testene følger best practises.
* Spesifikasjonsansvarlig: 
  * Ansvarlig for spesifikasjonene av produktet til enhver tid. Skal ha hovedansvar for at spesifikasjonen er riktig for det stadiet vi er på. Ansvarlig for at brukerhistorier og akseptansekrav er skrevet bra og håndtere problemer rundt dette.
* Kundeansvarlig /kontakt: 
  * Å innhente svar på spørsmål gruppemedlemmene har, fra kunde. 
  
Vi følte at dette var en hensiktsmessig måte å fordele de forskjellige oppgaver som trengs over i roller. Vi har nå forskjellige gruppemedlemmer som har hovedansvar over visse deler av kodebasen, spesifikasjoner og en Teamlead som har et overordnet ansvar for at alt som skal gjøres blir gjort. Vi fordelte rollene etter ønske og klarte å gjøre det på en ryddig måte hvor alle fikk en rolle de selv hadde ønsket. Vi håper denne fordelingen vil fungere bra og tillate oss å fordele ansvaret på en slik måte at vi har noen som har kunnskap til å hjelpe andre uansett område. 

## Metodikk:
Vi vil fokusere hovedsakelig på projectboardet og har dermed en projektmetodikk som følger kanban i en stor grad. 
Vi har også lyst å bruke parprogrammering og testdrevet utvikling der det kan være hensiktsmessig.
Vi planlegger å bruke standup møte fra scrum på hver gruppetime.

## Møter og kommunikasjon:
Vi har fastsatt ett ekstra møte hver uke mandag 12-14. Vi legger ekstra møter etter behov, når det er nødvendig.
Vi vil kommunisere via discord eller messenger chat for å planlegge / diskutere mellom møter.


For den første innleveringen har vi planlagt arbeidsfordeling slik:
Kodeansvarlig tar hovedansvar for å fremstille de mest grunnleggende elementer som skal til for å oppfylle krav til første innlevering.
Teamlead og brukerhistorie-ansvarlig tar ansvar for innlevering av tekst og brukerhistorier.
Testansvarlig er hovedansvarlig for testing av koden skrevet
Libgdx ansvarlig bistår både til kode og test etter behov.
Kundeansvarlig fremstiller spm til gruppeleder og hjelper til med tekstinnlevering.


## En kort beskrivelse av det overordnede målet for applikasjonen:

Målet er å få et produkt som gjør at flere personer kan spille en replika av "RoboRally" med hverandre over internett.
Foreløpig er målet å fremstille et minimal viable product, MVP, hvor vi legger inn de mest grunnleggende funksjonene for å få dette til.
Vi fremstiller brukerhistorier til disse grunnleggende funksjoner og legger med akseptansekrav og arbeidsoppgaver.

## Brukerhistorier:
#### 1:
1. As a user i want to be able to see the board so I can keep track of gameplay.

**Akseptansekrav:** Need to be able to draw the board to the user's screen.
**Arbeidsoppgaver:** Lage et board og vise det frem.

#### 2:
1. Som spiller vil jeg at en brikke skal vises slik at jeg vet hvor på brettet jeg står
2. Som spiller vil jeg at alle brikker skal vises slik at jeg kan planlegge turen min.

**Akseptansekrav:** En brikke kan vises på brettet.
**Arbeidsoppgaver:** Vise en brikke på spillebrettet

#### 3:
1. Som spiller ønsker jeg å kunne flytte en brikke for å gjennomføre turen min.
2. Som utvikler ønsker jeg å kunne flytte en brikke (rundt på brettet) for å teste ulike brettfelts effekter.

**Akseptansekrav:** En brikke kan flyttes (via konsoll komandoer, eller knapper, tastatur) fra en rute på spillebrettet til en annen.
**Arbeidsoppgaver:** En funksjon som kan flytte en brikke fra et sted på brettet til en annen og en måte å bruke denne funksjonen slik at den kan testes.


#### 4:
1. Som spiller ønsker jeg å ha flaggene tydelig markert, sånn at jeg finner de med en gang jeg spiller

**Akseptansekrav:** Kan tydelig se flaggene på brettet
**Arbeidsoppgaver:** legge til og markere flagg på brettet

#### 5:
1. Som spiller ønsker jeg å korrekt bestemme når en har besøkt flagg, for å bestemme vinner

**Akseptansekrav:** Vite når en har besøkt flagg
**Arbeidsoppgaver:** Registrere når en har besøkt flagg

#### 6:
1. Som [spiller] har jeg lyst å spille online med mine venner.

**Akseptansekrav:** Jeg kan se og interagere med samme brett som en annen via internett.
**Arbeidsoppgaver:** Vi har foreløpig ikke nok forståelse av dette til å lage konkrete arbeidsoppgaver.

#### *Vi har som hensikt å ferdigstille punkt 1-5 ved denne innlevering og begynne å planlegge for punkt 6 og andre utvidelser av kodebasen.*


## Diskusjon om hvordan det har gått:

Project boardet: Etter å ha satt oss inn i project boardet og hvordan det funker så har 
vi lagt en plan om hvordan vi kan bruke det bra og har blitt enige om hvordan vi legger inn
oppgaver, issues, user stories o.l. Vi har ikke klart å bruke det så bra i denne første innleverings
perioden men med bestemmelsene som er gjort skal ette gå bedre i fremtiden.

User stories: Vi har klart å bruke user stories på en hensiktsmessig måte og beskrive målene
vi ønsker å oppnå under denne innleveringen.

#### *Vi syns vi har god kommunikasjon og klart å diskutere oss frem til løsnigner. Alle meninger blir hørt.*

## Koding:
Det har blitt ubalansert med commits, men det er såpass mye å sette seg inn i for å starte prosjektet, 
slik at en person tok hovedansvaret. Dette gjorde det også enklere å lage denne grunnleggende kodebasen
som vi kan samarbeide om senere og bygge på. Selv det mest grunnleggende krevde å skrive mange forskjellige funksjoner
og klasser og derfor ble de arbeidsoppgavene vi hadde satt veldig knytt sammen og en person sto for alle bidrag til dette.
Vi håper derfor at det senere vil bli mer gjevnt når det gjelder bidrag til kodebasen, når det blir mindre oppgaver som kan gjennomføres eller større oppgaver som kan stykkes opp. 


## Fremtidige planer angående implementering av nettverk:
Vi har ikke sett på hvordan nettverk fungere inne i libgdx men vil lage konkrete planer når vi har satt oss inn i det.
Vi vil prioritere å få til å vise samme brett til spillere på forskjellige datamaskiner og at de skal kunne flyttes på det brettet samtidig.
Vi tenker å gjøre dette først, og implementere simultan bevegelse, altså alle gir en bevegelsekommando og de blir gjennomført i sekvens av spillet.
Etter at vi har fått til dette vil vi begynne å se på mer funksjonalitet til spillet som f.eks. spillekort, skade osv. 


## Problemer / kjente bugs / feil ved produktet vårt:
Spillet er turn-based istedenfor å følge reglene til spillet.
Veggene tar opp en hel rute istedenfor å være mellom.
Kollisjon ikke implementert og medfølgende grafiske bugs pga. dette
Flaggene krever ikke å bli plukket opp i rekkefølge slik reglene tilsier.
Du kan bare move en til av gangen. (dette pga. vi ikke har programkort).



