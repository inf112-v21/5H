# Oblig 2 
## 5H

##Deloppgave 1

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
### Krav

#### Brukerhistorier

#### Akseptansekrav

#### Arbeidsoppgaver

---
## Deloppgave 3
### Kode:

#### Bygging: 
- Vi har alle windows som OS og bruker Travis for å sjekke om det bygger på Linux/Mac.

#### Klassediagram:


#### Tester
**Automatiske tester:**
- g

**Manuelle tester:**

*@BeforeEach*
- Setup:
    - Start the game (check the how-to-play guide)

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