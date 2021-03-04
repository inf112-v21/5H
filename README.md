[![Build Status](https://travis-ci.com/inf112-v21/5H.svg?branch=master)](https://travis-ci.org/github/inf112-v21/5H)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/59c74c9604594cb0a07585f2dd1d4f45)](https://www.codacy.com/gh/inf112-v21/5H/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=inf112-v21/5H&amp;utm_campaign=Badge_Grade)

# Robo Rally
###INF112 5H

---


### Hva er RoboRally?
Roborally er et spill som involverer flere spillere, og lar dem kontrollere hver sin robot på et spillebrett. 
Roboten kontrolleres via programkort som hver spiller velger ut for sin robot, og robotene beveger seg så ut ifra de valgte kort-kommandoene. 
Man vinner ved å være den første til å besøke alle flaggene på brettet!

### Hvordan installere
TBD

### Starte ett spill
1. Kjør spillet
2. Velg om du skal være server eller klient

Server:
1. Velg hvor mange spillere dere er (max 4)
2. Velg porter for å la de andre spillerne koble seg igjennom
3. Vent på at alle andre kobler seg til
4. Nå starter spillet og kjører av seg selv!

Klient:
1. Skriv inn IP-adressen til Hosten
2. Skriv inn portene hosten valgte
3. Sjekk at du konnekter og vent på de andre spillerne
4. Når alle er tilkoblet starter spillet og du kan begynne å spille

### Spilleregler
For hver Omgang får hver spiller utdelt 9 kort. Spilleren må så velge seg ut
5 av disse kortene vha. nummertastene på tastaturet. 
Hver omgang består av 5 runder. I hver runde beveger roboten din seg ut ifra programkortet 
som du valgte ut for den runden. Hvis du står på ett flagg på slutten av runden er du nærmere å vinne.
Vinn ved å være den første til å ha roboten sin innom alle tre flaggene vinner!



### Kjente Feil og mangler

- Foreløpig eksisterer ikke skade i spillet og man kan ikke power down.
- Roboter skyter ikke laser
- Brettet består bare av simple felt, ingen rullebånd, skiftenøkler osv.
- Vegger tar opp en hel brikke på brettet
- Flaggene kan besøkes i hvilken som helst rekkefølge
- Roboter kan gå igjennom hverandre