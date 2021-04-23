# Oblig 3 Gruppe 5H

## Deloppgave 1

### Retrospektiv(denne oblig):

#### Roller i teamet og ProsjektMetodikk:
Rollene (som vi endret litt på forrige gang) fungerer fortsatt bra. Vi har alle komt godt inn i rollene våre og vi arbeider bra sammen.
Vi liker fortsatt prosjektmetodikken. Vi er bedre på kanban nå enn noen gang.
Vi har også arbeidet litt annerledes enn tidligere da vi har bare laget mange mulige oppgaver inn på project-boardet og latt alle velge hva de vil arbeide med.
Project-boardet har dermed vært enda mer sentralt enn tidligere. Dette har fungert bra og vi har hatt fått gjort mye (like mye hvis ikke mer enn forrige obliger)
selvom ingen delegerte direkte. Vi føler at vi nå har blitt veldig komfortable med project-boardet, selvom vi av og til er litt sene på å oppdatere det.



#### Kommunikasjon og gruppedynamikk:
- Vi har hatt litt andre møtetider + ekstra møter i forhold til i forrige obliger og det har fungert bra.
- Vi kommuniserer veldig bra nå da vi har blitt bedre kjent med hverandre og mer komfortable og alle har lettere for å ta opp noe, hvis det trengs.
- Vi klarer fint å kommunisere hva som spesifikt trengs av arbeid modifikasjoner for å utføre en arbeidsoppgave, da fra en person som har jobbet med denne delen av
kodebasen til den som skal utføre oppgavene.

### Retrospektiv av hele prosessen:

**Hva som gått bra:**
- Kanban og standup har gått bra.
- Løsning av tekniske problemer i felles har gått bra.
- God kommunikasjon fra start
- Prioriteringer har gått bra. At vi fullførte MVP først og fokuserte senere på andre ting
f.eks. GUI.
  
**Hva vi ville gjort annerledes:**
- Litt mer parprogrammering
- Burde brukt stages og tiledmap fra begynnelsen. (for stages vi brukte aldri tiledmap)
- Gått igjennom tutorials i libgdx (og KryoNet) i felles slik at alle hadde lik forståelse. 
    - Da kunne vi også kanskje ha funnet ut av stages og tiledmap tidligere som hadde gjort noen av implementasjonene senere i prosjektet lettere
- Tatt med en debug mode / beholdt noen av tastatur inputsene vi hadde fra først oblig til å bruke til testing / debugging.
- Prøvd å fordele arbeidsooppgavene jevnere.


---
## Deloppgave 2

### Prioriteringer

Våre prioteringer for denne innleveringen var å fikse alt som ikke fungerte helt og gjøre ferdige de siste funksjonene slik at vi  hadde et ganske ferdig spill, med de fleste funksjoner
som er forklart i spillereglene. Våre prioriteringer var derfor slik:

1. Prioritet:
- Antall kort som deles ut skal være lik antall PC(hvor mye skade du har tatt). Slik at spillere som har tatt skade får mindre kort å velge fra.
- Hvis antall kort utdelt er mindre enn 5, skal kort fra forrige tur "låses" fast, man begynner med kort nr 5 og jobber seg fremover.
    - I implementasjonen av dette må vi også fjerne kort som er låst fra dekket, da det lages ett nytt dekk for hver omgang.
- Legge til siste felt som mangler av de som kjøres på slutten av hvert register (rullebånd og gear)
  - Også lage noen junit tester for dette
- Legge til slik at brettfeltene med funksjon kjøres på korrekt tid, samt fyre av laser og plukke opp flagg i denne fasen
    - Herunder må det lages en del support metoder for å få dette til. Disse vil ha ansvar for å kjøre rullebånd fyre lasere etc.
- Vise score (antall flagg plukket opp i GUI)
- Legge til rette slik at spillere kan powerdown roboten sin (dette krever også en knapp for dette)
- Lage grafiske elementer til nye bretteleementer
- La spillerne velge når de vil sende kortene sine inn, ikke la det skje automatisk når du har valgt 5 kort (krever knapp)

2. Prioritet:
- La spillerne klikke på kort for å velge dem
- Legge til musikk (med mute knapp)
- Velge brett via GUI
- Lage ett eller flere brett som implementerer alle felt-typene vi har laget
- Knapp som viser frem regler
- Knapp som viser info om spillet (credits)



### Brukerhistorier
Merk at ikke alle punkter nevnt ovenfor har sin egen brukerhistorie da de er bugfixes / forbedringer på oppgaver med userstories fra tidligere innleveringer.
F.eks. vi hadde brukerhistorie på grafiske representasjon av bretteti oblig1, så forbedringer av grafikk trenger ikke egen brukerhistorie her.


**Kort reflekterer skade og låses hvis man får utdelt < 5 kort**
*UserStory:*
- Som en spiller ønsker jeg at når motspillerne har tatt skade får de utdelt mindre kort slik at de har mindre valg for bevegelse
- Som en spiller ønsker jeg at når jeg får utdelt færre enn 5 kort, blir noen av kortene jeg brukte forrige runde værende igjen slik at jeg har nok kort til å programmere roboten min.

*Arbeidsoppgaver:*
- Dele ut kort basert på skade tatt (9 kort hvis uskadet, 0 kort hvis fullstendig skadet)
- Låse kort fast hvis spiller får utdelt færre enn 5 kort.
- Fjerne låste kort fra dekket slik at det ikke blir to kopier av hvert spill
- Sørge for at låste kort vises i GUI
- Sørge for at spiller fortsatt bare får sende 5 kort og ikke kan erstatte låste kort.
  
*Akseptansekrav:*
- Alle spillere får utdelt korrekt anntall kort basert på skade
- Kort låses og dette reflekteres i GUI og spill-oppførsel
- Duplikat av kort eksisterer ikke i dekket pga. låste kort.

**Rullebånd / ConveyorBelt**
*UserStory:*
- Som en brettdesigner ønsker jeg at RoboRally har rullebånd slik at jeg kan lage spennende brett.
- Som en spiller ønsker jeg at rullebånd er i spillet slik at jeg kan planlegge mer avanserte bevegelser

*Arbeidsoppgaver:*
- lage grafikk (midlertidig)
- Implementere en rullebånd klasse
- Implementere en ekspress rullebån klasse
- La spillet lese rullebånd felt fra brett
- Implementere en metode for å kunne kjøre rullebåndene slik at spillere på dem flyttes.
- Skrive junit tester som tester at rullebånd utfører sine funksjoner

*Akseptansekrav:*
- Brett kan inneholde rullebånd
- Rullebånd vises på spillebrettet sitt GUI
- Ekspressrullebånd kjøres to ganger etter hver register
- Rullebånd kjøres en gang etter hver register
- Når et rullebånd kjøres flyttes alle spillere på det ett felt i korrekt retning. tar også hensyn til kollisjon.
- Junit tester for rullebånds kjøring bekrefter at de rullebåndene aktiveres korrekt og flytter spillere korrekt.

**Tannhjul / Gear**

*UserStory:*
- Som en brettdesigner ønsker jeg at RoboRally har tannhjul slik at jeg kan lage spennende brett.
- Som en spiller ønsker jeg at tannhjul er i spillet slik at jeg kan planlegge mer avanserte bevegelser

*Arbeidsoppgaver:*
- Lage grafikk som også indikerer retning(midlertidig)
- Implementere en tannhjul klasse
- La spillet lese tannhjul (inkludert retning) felt fra brett
- Implementere en metode for å kunne kjøre tannhjul slik at spillere på dem roteres.
- Skrive junit tester som tester at tannhjul utfører sine funksjoner

*Akseptansekrav:*
- Brett kan inneholde tannhjul
- Tannhjul vises på spillebrettet sitt GUI
- Tannhjul kjøres en gang etter hver register
- Når et tannhjul kjøres roterers spilleren i korrekt retning
- Junit tester for tannhjul som bekrefter at spillere roteres i korrekt retning.

**Fase for å kjøre brettfelt, skyte laser, plukke opp flagg**

*UserStory:*
- Som spiller ønsker jeg at lasere skal skytes på slutten av hver tur slik at jeg kan planlegge bevegelsene mine bedre. 
- Som spiller ønsker jeg at flagg skal kunne plukkes opp på slutten av hver tur slik at man belønnes for godt planlagte bevegelser.

*Akseptansekrav:*
- En fase etter hver spiller har bevegd seg en gang, der blant annet lasere skal fyres av.

*Arbeidsoppgaver:*
- Finne en måte å vite når alle spillere har bevegd seg x ganger, og kalle de nødvendige funksjonene her.

**Vise score/flagg i GUI**

*Userstory:*
- Som bruker har jeg lyst til å se hvor mye poeng jeg har slik at jeg kan vite om jeg ligger godt an til å vinne eller ikke.

*Akseptansekrav:*
- Må kunne se score i GUI

*Arbeidsoppgaver:*
- Oppdater GUI til å ha plass til score
- Print score i GUI

**Brettvalg:**
*Userstory:*
- Som spiller ønsker jeg å kunne velge mellom flere brett å spille på, slik at jeg ikke alltid må spille det samme kjedelige brettet.

*Akseptansekrav:*
- For å velge brett
- Arbeidsoppgaver:

*Lage et menyvalg for å velge brett*
- Endre game til å bruke det valgte brettet
- Sørge for at riktig brett blir sendt til clients

**Submit cards funksjonalitet + knapp**
*Userstory:*
- Som bruker ønsker jeg å kunne verifisere at jeg ønsker å sende kort før de blir sendt, slik at jeg ikke velger feile kort med et uhell.

*Akseptansekrav:*
- Knapp for å bekrefte sending av kort

*Arbeidsoppgaver:*
- Implementer funksjonalitet for å bekrefte sending av kort ved hjelp av knapp/tastetrykk (Må endre logikk for når kort sendes i serverRenderLogic() og clientRenderLogic())

**Klikkbare kort**
*Userstory:*
- Som bruker ønsker jeg å kunne klikke på kort for å velge dem, da dette er det som er mest intuitivt for meg.

*Akseptansekrav:*
- Kunne bruke musepeker for å velge kort

*Arbeidsoppgaver:*
- Implementer stage
- Lag buttons for hvert kort (9stk)
- Plasser buttons slik at de overlapper med sitt kort
- Legg til listener på hver button som velger/velger vekk kortet

**Powerdown**

*Userstory:*
- Som [spiller] ønsker jeg å kunne ta en powerdown for å få nye liv slik at jeg ikke dør og/eller kan planlegge hvordan jeg skal vinne.
*Akseptansekrav:*
- Klikk på powerdown knappen for å aktivere powerdown neste runde.

*Arbeidsoppgaver:*
- Gjøre knappen klikkbar
- Fikse logikken
- Endre slik at powerdown skjer på neste tur.

**Knapp med regler**
*Userstory:*
- Som [spiller] ønsker jeg mulighet til å trykke på en info knapp som visuelt viser meg reglene for spillet.
*Akseptansekrav:* 
- En visuell trykkbar knapp som åpner et nett vindu som displayer reglene til spillet.
*Arbeidsoppgaver:*
- Skrive reglene til spillet
- Lage en sprite med symbolet (i)
- Lage en ny screen der reglene displayes.

**Musikk**

*Brukerhistorie:*
- Som [spiller] ønsker jeg å høre en bakgrunnsmusikk når spillet starter og ha mulighet til å mute musikken.
*Akseptansekrav:*
- En theme låt begynner ved oppstart av spillet. En visuell knapp er også tilgjengelig for å stoppe den samme musikken.
*Arbeidsoppgaver:*
- Hyre en musikker til å lage en theme låt til spillet.
- Få orden på knapp logikken.
- Lage en Sprite til mute knappen
- Legge theme sangen til i programmet.


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
    - Angående valg av porter så kan du bruke port 1 hvis du spiller lokalt. Hvis du spiller via internett må du velge egne porter og hosten må portforwarde. Hvilke porter som er tilgjengelig for dette er avhengig av Internettleverandør, nettverksinstillinger og installert programvare. For vår testing har vi brukt port 3074.
3. I steget over må det også velges brett, ett av de tilbudt. Ta gjerne og åpne opp alle brettene på egenhånd (velg 1 spiller) for å sjekke dem ut på forhånd. Hvis du velger flerspiller modus
   dukker ikke brettene grafisk opp før spiller starter.
4. Avhenger om du vil spille alene eller med andre
    - Dersom du ønsker å spille med andre må porten du velger være port-forwardet i ruter instillingene dine,
      da følger de andre samme steg som deg, men velger "client" i punkt 2. Du som server må finne ip-adressen din, eksempelvis på
      https://whatismyipaddress.com/. Client må skrive inn denne ip adressen (IPv4), så portene du valgte i steg 2.
    - 3.2 Dersom du ønsker å spille aleine (for testing), kjør Main2, velg "client", velg "localhost" som ip, og samme porter du valgte i steg 2.
5. Når antall spillere som er med i spillet er lik antall spillere du valgte når du satt opp "server" starter spillet,
   og alle får tildelt en hånd. Denne blir for nå printet i konsollen. Du har bevegelse [prioritet]. Du velger disse ved å bruke
   tastene 1-9. Når 5 kort er valgt blir disse sendt til serveren, og du avventer resten av spillerene. Når alle har sendt bevegelser
   skjer bevegelsene steg for steg for alle spillere. (KNOWN BUG: Desync, noen ganger skjer feile ting for Client, jobber med å fikse dette)

For å bygge koden:
- Kjør `mvn clean install`. (Testet til å funke på alle platformer, skal få build success)

#### Bygging:
- Vi har alle windows som OS og bruker Travis for å sjekke om det bygger på Linux/Mac. Ifølge Travis bygger prosjektet vårt.

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

De automatiske testene våre dekker store deler av kodebasen. For mer detaljert forklaring av hva de automatiske testene dekker, se ObligatoriskOppgave2.md og ObligatoriskOppgave3.md .

Fra forrige gang har det ikke skjedd store endringer. Det har vært noen forandringer for testen av score i PlayerTest da flagg nå plukkes opp litt annerledes. Det har også vært små
forandringer i testene for kortutdeling som nå tar høyde for skade.

Det er laget noen basic junit tester for den nye brett feltene, ConveyorBelt, ExpressConveyorBelt og Gear. De andre implementerte brett-funksjoner som f.eks. laser hadde allerede tester
implementert. 


**Manuelle tester:**

Det er laget noen manuelle tester for oppførsel vi ikke kan teste automatisk. Dette inngår f.eks.
å se at det å velge et kort og så sende det etter å ha valgt 5 kort vil føre til at roboten oppfører seg slik
kortvalgene tilsier.

*@BeforeEach*
- Setup:
    - Start the game (se teknisk produkt oppsett for instrukser)
    - For GUI the specified amount of players(chosen during launch) needs to join before GUI is rendered. 

*MoveForwardTest:*
- Select the move card. The player Sprite should then move forward.

*RotateRightTest*:
- Select a rotate right card. The player Sprite should then rotate 90 degrees clockwise.

*RotateLeftTest*:
- Select a rotate left card. The player Sprite should then rotate 90 degrees counterclockwise.

*OutOfBoundsTest*
- Select cards so that the player goes out of bounds. The HP should then go down 1. (Check HP in GUI)
- Out of bounds is either a hole or outside the playing board.

*Flag/win Test*
- Select cards so that the player goes on all the 3 flags. The player should then win. PLayer needs on flag at end of register.

*Cards tests*
- You should be able to click cards to select/unselect them. 
- You should be able to use keyboard to select/unselect cards.
- The submit cards button should submit your cards when you click it if you have 5 cards selected.

*PowerDown test*
- During card select you should be able to click powerdown button, to announce a powerdown next turn.
- The next turn you should be dealt no cards and PC should be set to 9 (check pc in GUI).
- After a turn of powerdown you should receive cards again.
-
*Mute button test*
- Mute button should be clickable and toggle on and off music.

*Info button test*
- Info button renders an image that displays the rules of the game. Clicking it again removes the image and shows the standard UI again.

*End-of register tests*
- When every player has done one move, the following should happen:
    - Express ConveyorBelt should move players on them one
    - All conveyorbelts should move players on them one
    - Gears should rotate players on them
    - Lasers should be fired
    - Flag picked up if you stand on them now.
- This should happen after each time each player has executed one move (one card).
- This test is to test that the conveyorbelts etc. are actually called, not that their function is correct as junit tests exist for that.

*Start of game GUI test*
- HP and PC should be rendered and HP=3, PC=9. Score should also be rendered and set to 0.



#### Produktets funksjonalitet:
Vårt produkt har nå tilnærmet all funksjonalitet som er forventet av RoboRally slik det er beskrevet i reglene.

Vi har føløgende mangler fra forventet oppførsel:
- Vegger tar opp ett helt felt på brettet
- Mangler nye respawn points / skiftenøkkel
- Har bare et stort 12x12 brett, kan ikke ha to slike brett eller brettet med startpunkter til robotene slik instruksjoneen viser.
    - I regelboken er det beksrevet ett spesielt brett med start felter som spiller som brukes sammen med alle de ulike brettene
    - Det skal også være mulig å sette sammen flere av brettene, dette er ikke mulig i vår versjon, bare ett brett om gangen.
- Bare 4 spillere (dette kan vi endre uten store tekniske endringer, men pga. brett restriksjonene kan det være lurt å restriktere antall spillere)
- Det er noen restriksjoner rundt rullebånd, to av dem kan ikke peke mot hverandre / på samme felt.
    - I reglene er det beskrevet at hvis to rullebånd prøver å sende spillere til samme felt vil ingen av dem flyttes. Dette vil ikke skje i vårt spill,og brett kan da ikke inneholde
    slike situasjoner.
- Det er ingen timer som starter når en person er den eneste som ikke har submittet kort
    - Når det er bare en spiller som ikke har submittet sine kort, skal det startes en timer på 60 sekund. Når denne er over vil spillerens kort bli sendt inn, og hvis spilleren 
    ikke har valgt 5 kort enda vil det velges tilfeldige kort for å fylle hånden før den sendes.
      
Bortsett fra dette har vi et spill som vi føler er et godt eksempel på RoboRally med god brukeropplevelse og lett-brukelig UI.

#### Kommentarer til kode og utførelse

I denne innleveringen er commits igjenn ujevne. Dette er til dels pga. forksjellige gruppemedlemmer committer oftere enn andre. Noen (f.eks. Jonas) gjorde flere mindre implementasjoner
mens andre gjorde større implementasjoner (her er det snakk om kompleksitet ikke antall linjer kode). Noen bidro også f.eks. med grafikk som vil bare reflekteres som en commit selvom det
står mange timers arbeid bak grafikken. Team-lead (Jonas) får også en del ekstra commimts, da han fører referat og skriver innelverings tekstene.
Når det gjelder det faktiske arbeidet som er gjennomført for denne obligen er det mye jevnere enn commit historien tilsier.