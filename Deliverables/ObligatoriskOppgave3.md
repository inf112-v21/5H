# Oblig 3 Gruppe 5H

## Deloppgave 1

### Retrospektiv: 

#### Roller i teamet:
Vi har alle utført rollene vi har i Teamet, så langt har det gått bra og vi planlegger ikke noen bytter.
Vi har ikke brukt kundekontakten vår godt nok men har heller ikke hatt så mange spørsmål så langt.
Likevel vi bør prøve å ha viktige spørsmål klar på forhånd og fremstille de til kundekontakt, Henrik
slik at han kan sende spørsmålene videre til de relevante personer på forhånd. På denne måten
kan vi få gode og forberedte/ svar fra lab-leder/foreleser.

#### Kommunikasjon og gruppedynamikk:
- Alle har bidratt mer i diskusjoner o.l. 

#### Projekt Metodikk:
- Vi føler at vi iløpet av oblig 2 fikk til Kanban prinsippene enda bedre enn før.
Vi fortsetter med dette og å bruke kanban og prøve å bruke det enda bedre enn før. Vi fortsetter også med standup
  da vi føler det er en veldig hjelpsom metodikk for å ha gode gruppemøter.
##### Oppsumering av forbedringspunkter for denne oblig:
- Alle committe til kodebasen
- 

#### Metoder vi ønsker å ta i bruk:




---
## Deloppgave 2
### Brukerhistorier




#### Forklar kort hvordan dere har prioritert oppgavene fremover:
Vår prioritet denne innleveringen har vært å legge til GUI del til spillevinduet. 
Utover dette har vi lagt til mindre funksjoner til spillet og fikset feil/bugs.
#### Bugs
Se readme / project board for bugs.

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
    - Angående valg av porter så kan du bruke port 1 hvis du spiller lokalt. Hvis du spiller via internett må du velge egne porter og hosten må portforwarde. Hvilke porter som er tilgjengelig for dette er avhengig av Internettleverandør, nettverksinstillinger og installert programvare.
3. Avhenger om du vil spille alene eller med andre
    - Dersom du ønsker å spille med andre må porten du velger være port-forwardet i ruter instillingene dine,
da følger de andre samme steg som deg, men velger "client" i punkt 2. Du som server må finne ip-adressen din, eksempelvis på
https://whatismyipaddress.com/. Client må skrive inn denne ip adressen (IPv4), så portene du valgte i steg 2.
    - 3.2 Dersom du ønsker å spille aleine (for testing), kjør Main2, velg "client", velg "localhost" som ip, og samme porter du valgte i steg 2.
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
- Røde firkanter er private
  Selve boksene benytter seg også av vanlig UML syntax. 3 ledd. Navnet på klassen, et felt for variabler, tilsutt et felt for metodene.

Klassediagramene er laget for de ulike pakkene: skeleton.app, skeleton.app.cards, skeleton.app.net, skeleton.app.sprites

#### Tester
**Automatiske tester:**

De automatiske testene våre dekker store deler av kodebasen. Vi tester at Player fungerer korrekt og andre objekter knyttet til den.
Vi tester også nettverksdelen, hvor vi tester at vi kan connecte klient til server, og sende klasser frem og tilbake mellom dem, og at dette
blir korrekt registrert av Listeners. Det har vært noen problemer av og til når Travis kjører automatiske tester på nettverket,
at connections ikke har blitt closed til tross for at vi closer connections i en @AfterEach metode i testene. Vi prøver å finne ut hvorfor dette skjer,
men feilen har dukket opp rett før innlevering, og skjer bare av og til og ikke i IntelliJ så vi har problemer med å løse de. Hvis tester feiler, kan det være at de vil passere etter å bha blitt kjørt om igjen.


Vi har også tester på kort, i at de blir lagd korrekt og kan leses. Vi tester også at dekket består av alle de kortene det skal
ifølge reglene, og at prioritetene til de forskjellige kortene er unike. Vi har ikke lagd automatiske tester til noen deler
som krever grafisk interaksjon og har dermed Manuelle tester på denne delen av kodebasen.

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

#### Produktets funksjonalitet:
Vårt produkt oppfyller MVP, fra forrige oblig.



#### Kommentarer til kode og utførelse
