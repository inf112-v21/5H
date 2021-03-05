# Oblig 2 Gruppe 5H

## Deloppgave 1

#### Roller i teamet:
Vi har alle utført rollene vi har i Teamet, så langt har det gått bra og vi planlegger ikke noen bytter.
Vi har ikke brukt kundekontakten vår godt nok men har heller ikke hatt så mange spørsmål så langt.
Likevel vi bør prøve å ha viktige spørsmål klar på forhånd og fremstille de til kundekontakt, Henrik
slik at han kan sende spørsmålene videre til de relevante personer på forhånd. På denne måten
kan vi få gode og forberedte/ svar fra lab-leder/foreleser.

#### Kommunikasjon og gruppedynamikk:
Noen snakker mye, andre lite. Vi må ta ansvar for å la alle komme til ordet, og at de som ikke bidrar
mye muntlig må prøve å bidra mer. Dette må begge "sider" prøve å forbedre oss på.

#### Standup: 
Det har fungert bra og vi vil fortsette med dette.

#### Kanban: 
Vi har kanskje ikke brukt projectboardet til sitt fulle potensial. Vi har f.eks.
ikke brukt det noe særlig til å fordele oppgaver. Dette må vi forbedre og legge inn oppgaver før de fordeles, ettersom akkurat nå
legger vi de til basert på hvilke oppgaver vi har begynt på. Ved å endre bruken vår av projectboardet vil vi kunne få bedre oversikt over fremdiften og planen for prosjektarbeidet.

##### Oppsumering av forbedringspunkter for denne oblig:
- Fordele oppgaver mer og gjerne bruke projectboardet til å gjøre dette.
- Utnytte rollene mer
    - utnytte i større grad da f.eks. kundeansvarlig til å stille spørsmål
    - spesifikasjonsansvarlig til brukerhistorier etc.
    
##### Metoder vi ønsker å ta i bruk:
- Vi vil bruke klassediagram til å hjelpe oss med å visualisere prosjektet, og planlegge fremtiden
og dermed arbeidsfordeling.
  


---
## Deloppgave 2
### Brukerhistorier

#### [6] Spille fra flere maskiner (vise brikker for alle spillere, flytte brikker for alle spillere): 
**Brukerhistorie:** Som [spiller] har jeg lyst å spille online med mine venner.

**Akseptansekrav:** Jeg kan se og interagere med samme brett som en annen via internett.

**Arbeidsoppgaver:** 
- Sette opp Gdx.net
- Kunne sende og motta HTTP requests
- Bruke Gdx.net til å spille sammen to eller flere spillere på samme brett

#### [7] Dele ut kort: 
**Brukerhistorie:** Som [spiller] ønsker jeg å mota tilstrekkelig med kort, mens jeg fortsatt er i livet.

**Akseptansekrav:** Deltagende spillere mottar 9 kort (Implementasjon der dette avtar ettersom man mister liv er ikke med i MVP)

**Arbeidsoppgaver** 
 - Card klasse som representerer ett kort
 - Deck klasse som inneholder alle kortene som trengs for RoboRally
 - Deale en hånd til spiller
    - Lage en Hand klasse som holder kortene
    - Legge til tilfeldige kort fra Deck til Hand
    - Metode for å sende hånden fra serveren til klienten

#### [8] Velge 5 kort: 
**Brukerhistorie:** Som [spiller] ønsker jeg å kunne huke av fem kort blant de tildelte kortene mine.

**Akseptansekrav:** Tildelte kort vises frem for spilleren og spilleren står fritt til å velge fem kort blant de tildelte.

**Arbeidsoppgaver** 
- La klienten motta kort fra Server ved å legge til i ClientListener
- Metode for å registrere 5 kort fra tastaturet
- Velge ut kortene til en ny hånd og sende dem tilbake
- Legge til I ServerListener slik at den kan motta hånden og bruke den.

#### [9] Bevege robot ut fra valgte kort: 
**Brukerhistorie:** Som [spiller] ønsker jeg at min robot etterligninger bevegelsene anvist av mine valgte kort denne runden.

**Akseptansekrav:** Roboten er i stand til å bevege seg etter angitte instrukser.

**Arbeidsoppgaver:** 
- Metode for å ta et kort og gjøre en bevegelse ut i fra kortet
- Sørge for at prioriteten til kortene blir tatt med i vurdering av hvem som skal flytte først
- Sørge for at bevegelser blir sendt mellom spillere slik at det oppdateres for alle brukere



#### Forklar kort hvordan dere har prioritert oppgavene fremover:
Vår første prioritering denne innleveringen var å sette opp nettverksfunksjonen, til dette har vi brukt KryoNet. Vi gjorde dette først med å klare å sende klasser frem og tilbake mellom server og klient. Først brukte vi bare dette til å se om brikkene klarte å bevege seg med kommandoer fra klient, senere introduserte vi også at brettet blir oppdatert hos klienten etter flytt. Etter dette begynte vi på kort klassene og holder av kort (hånd og dekk), og passet på at disse ble skrevet på en måte som var kompatibelt med KryoNet. Imens kortene ble skrevet tok vi også og refaktorerte nettverksklassen slik at den var separat fra game. Etter at kort og nettverksrefaktorering var ferdig kunne vi begynne på å bevege seg basert på kort, og funksjoner for å velge kort. Dette ble gjort sist da det var avhengig av de forrige stegene.

#### Bugs 
- Issue #40
- Issue #43 



---
## Deloppgave 3

#### Teknisk produktoppsett 
Software som trengs:
- Last ned java versjon 13 eller nyere (https://www.oracle.com/java/technologies/javasedownloads.html)
- En IDE, eksempelvis IntelliJ (https://www.jetbrains.com/idea/download/#section=windows)
Setup:
- Klon prosjektet fra github / last ned .zip med nyeste tagged commit
	
For å kjøre koden:
    1. Åpne prosjektet i IntelliJ (eller annen IDE), kjør Main.java.
    2. Velg "server", antall spillere du ønsker i spillet, og porter. (hvis du velger 1 spiller så      avslutter spillet etter en runde, da du vinner)
    3.1 Dersom du ønsker å spille med andre må porten du velger være port-forwardet i ruter instillingene dine,
        da følger de andre samme steg som deg, men velger "client" i punkt 2. Du som server må finne ip-adressen din, eksempelvis på 
        https://whatismyipaddress.com/. Client må skrive inn denne ip adressen (IPv4), så portene du valgte i steg 2.
    3.2 Dersom du ønsker å spille aleine (for testing), kjør Main2, velg "client", velg "localhost" som ip, og samme porter du valgte i steg 2. 
    4. Når antall spillere som er med i spillet er lik antall spillere du valgte når du satt opp "server" starter spillet,
            og alle får tildelt en hånd. Denne blir for nå printet i konsollen. Du har bevegelse [prioritet]. Du velger disse ved å bruke 
            tastene 1-9. Når 5 kort er valgt blir disse sendt til serveren, og du avventer resten av spillerene. Når alle har sendt bevegelser
            skjer bevegelsene steg for steg for alle spillere. (KNOWN BUG: Desync, noen ganger skjer feile ting for Client, jobber med å fikse dette)
 
For å bygge koden:
- Kjør `mvn clean install`. (Testet til å funke på alle platformer, skal få build success)

#### Bygging: 
- Vi har alle windows som OS og bruker Travis for å sjekke om det bygger på Linux/Mac.

#### Klassediagram:
Vi har generert klassedigram med Object Aid Explorer. Dette er en Eclipse Plugin som finnes her: https://marketplace.eclipse.org/content/objectaid-uml-explorer. 
ObjectAid generer ønsket klassediagram for valgte klasser, viser frem innhavende metodene og feltvariabler. Det kan også vise fram dependencies som vi også har gjort, dette vises frem med piler. 

De generert diagrammene benytter seg av samme symbolikk som UML digram gjør.
- Grønne sirkler står for public
- Rødefirkanter er private
Selve boksene benytter seg også av vanlig UML syntax. 3 ledd. Navnet på klassen, et felt for variabler, tilsutt et felt for metodene. 


#### Tester
**Automatiske tester:**
- g

**Manuelle tester:**

*@BeforeEach*
- Setup:
    - Start the game (se teknisk produkt oppsett for instrukser)  

*MoveForwardTest:*
- Select the move card. The player Sprite should then move forward.

*RotateClockwiseTest*:
- Select a clockwise rotation card. The player Sprite should then rotate 90 degrees clockwise.

*RotateCounterClockwiseTest*:
- Select a clockwise rotation card. The player Sprite should then rotate 90 degrees counterclockwise.

*OutOfBoundsTest*
- Select cards so that the player goes out of bounds. The HP should then go down 1. (Check output in console)

*HoleTest*
- Select cards so that the player goes in a hole. The HP should then go down 1. (Check output in console)

*FlagTest*
- Select cards so that the player goes on all the 3 flags. The player should then win (Order does not matter yet)

#### MVP:
Vårt produkt oppfyller MVP.

1. Vi kan vise et spillebrett
2. Vi kan vise spillebrikker på brettet
3. Vi kunne flytte brikker via piltaster men nå bruker vi kort
4. Robot kan besøke flagg
5. Robot vinner hvis den besøker flagg (foreløpig bryr ikke spillet seg om rekkefølgen på flaggbesøk)
6. Vi kan spille fra flere maskiner, både på lokalt nettverk men også via Internett
   - En spiller må opptre som Server 
   - De andre spillerne kobler seg til som klienter (maks 3 per nå)
7. Vi kan dele ut kort (fra et fullstendig dekk som er likt det fra det fysiske spillet)
8. En spiller får tildelt 9 kort og kan velge ut 5 av disse 
9. Disse valgte kortene programmerer spillerens robot og dens bevegelser

#### Kommentarer til kode og utførelse
Vi har fortsatt ujevne commits. Dette er til dels pga. parprogrammering hvor en person commiter arbeidet, og i githubs oversikt er det da den personen som får credit.
Det er også litt forskjell i størrelse på commit til de forskjellige gruppemedlemmene noe som også påvirker antall commits.
Det er fortsatt sånn at vi føler at main/game er ganske innfløkt og at derfor bare noen par har rørt filen. Vi prøver derfor å fordele andre oppgaver som omhandler
klasser utenom game.java for at alle skal få kodet, men det har sålangt har behovet for dette bare vært cards packagen. Vi fant network packagen like vanskelig
som det å lage det første brettet og dette ble også skrevet av en person og så refaktorert til å passe behov i etterkant.

Vi har litt kode som er ubrukt i form av RequestToClient klassen og dens tester (samt metoder i listener klassene knyttet til denne).
Denne klassen ble brukt når vi først implementerte nettverk til å sende beskje til klienter om at de måtte sende move. Dette ble overflødig da bevegelse ut ifra kort ble implementert.
Dette pga. klientens mottagelse av utdelte kort er  i seg selv et signal på at den må velge ut kort, og trenger ikke en selvstendig beskjed i RequestToClient for dette.
Men ettersom vi ikke er helt sikker på hvordan videre implementasjon av andre aspekter med spillet, vil fungere har vi metoden fortsatt inne i kodebasen i tilfellet det blir behov for den. 
Hvis vi kommer til et punkt hvor vi vet at vi ikke trenger å sende requests til klienten vil vi fjerne den.
