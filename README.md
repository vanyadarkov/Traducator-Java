# SmartTranslator – Serviciu de traducere texte 
### Introducere
Scopul acestei teme este de a crea o aplicație similară cu Google Translate care poate să traducă cuvinte sau propoziții dintr-o limbă în alta.

### Descrierea problemei
Firma la care lucrați are o colecție substanțială de dicționare explicative pentru mai multe limbi și vrea să creeze o aplicație care să rivalizeze cu Google Translate. Prin urmare, principala funcționalitate a aplicației este traducerea de cuvinte și propoziții.

Aplicația este dezvoltată de trei echipe separate, o echipă care se ocupă de scanarea dicționarelor și de procesul de OCR (Optical Character Recognition), o echipă care se ocupă de backend și una care se ocupă de frontend. Voi lucrați în echipa de backend care primește informațiile din dicționar de la prima echipă și trebuie să furnizeze date către echipa de frontend. 

Prima echipă v-a pus la dispoziție trei fișiere text ce conțin informații parțiale din dicționarele român,francez si rus în format JSON (JavaScript Object Notation). Restul de dicționare vor fi livrate la un alt moment din viitor. Structura unui element din dicționar este următoarea:
|Nr. linie|Exemplu dicționar român|Descriere câmpuri|
|--|--|--|
|1| { |Un obiect JSON este cuprins între { }, o listă este cuprinsă între [ ]|
|2|"word": "pisică",|Forma de dicționar în limba română|
|3|"word_en": "cat",|Forma de dicționar în limba engleză – disponibilă pentru orice cuvânt din orice dicționar|
|4|"type": "noun",|Partea de vorbire – noun, verb|
|5|"singular": ["pisică"],|Forma de singular a cuvântului|
|6|"plural": ["pisici"],|Forma de plural a cuvântului|
|7|"definitions": [|Listă de definiții pentru cuvântul curent|
|8|{||
|9|"dict": "Dicționar de sinonime",|Numele dicționarului|
|10|"dictType": "synonyms",|Tipul dicționarului – synonyms sau definitions|
|11|"year": 1998,|Anul publicării dicționarului|
|12|"text": ["mâță", "cotoroabă", "cătușă"]|Listă de sinonime sau de definiții ale cuvântului (în acest exemplu sunt disponibile 3 sinonime)|
|13|},||
|14|{||
|15|"dict": "Dicționarul explicativ al limbii române, ediția a II-a",|Numele dicționarului|
|16|"dictType": "definitions",|Tipul dicționarului – synonyms sau definitions|
|17|"year": 2002,|Anul publicării dicționarului|
|18|"text":["...","...","..."]|Listă de sinonime sau de definiții ale cuvântului (în acest exemplu sunt disponibile 3 definiții)|
|19|}||
|20|]||
|21|}||

Notă: În funcție de tipul părții de vorbire, câmpurile singular și plural pot avea următorul format:

|Parte de vorbire|Singular|Plural|
|--|--|--|
|Noun|Listă cu forma de singular a cuvântului|Listă cu forma de plural a cuvântului|
|Verb|Listă de conjugări la indicativ prezent singular în ordine persoana I, a II-a și a III-a|Listă de conjugări la indicativ prezent plural în ordine persoana I, a II-a și a III-a|

### Functionalitati implementate

1. Citirea din dicționare și salvarea informațiilor într-o singură colecție de date. Pentru citirea din JSON s-a folosit biblioteca [gson](https://github.com/google/gson). 
	- Se vor citi toate fișierele dintr-un folder de intrare care au extensia .json 
	- **Atenție** – nu știți de la început numărul lor
2. Metodă pentru adăugarea unui cuvânt în dicționar 
	- `boolean addWord(Word word, String language)`
	- Întoarce **true** dacă s-a adăugat cuvântul în dicționar sau **false** dacă există deja cuvântul în dicționar
3. Metodă pentru ștergerea unui cuvânt din dicționar ce primește ca parametru cuvântul și limba
	- `boolean removeWord(String word, String language)`
	- Întoarce **true** dacă s-a șters cuvântul în dicționar sau false dacă nu există cuvântul în dicționar
4. Metodă pentru adăugarea unei noi definiții pentru un cuvânt dat ca parametru
	- `boolean addDefinitionForWord(String word, String language, Definition definition)`
	- Întoarce **true** dacă s-a adăugat definiția sau **false** dacă există o definiție din același dicționar (dict)
5. Metodă pentru ștergerea unei definiții a unui cuvânt dat ca parametru
	- `boolean removeDefinition(String word, String language, String dictionary)`
	- Întoarce **true** dacă s-a șters definiția sau **false** dacă nu există o definiție din dicționarul primit ca parametru
6. Metodă pentru traducerea unui cuvânt
	- `String translateWord(String word, String fromLanguage, String toLanguage)`
	- Întoarce **traducerea** cuvântului word din limba **fromLanguage** în limba **toLanguage**
7. Metodă pentru traducerea unei propoziții
	- String translateSentence(String sentence, String fromLanguage, String toLanguage)
	- Întoarce traducerea propoziției sentence din limba fromLanguage în limba toLanguage
8. Metodă pentru traducerea unei propoziții și furnizarea a **3 variante** de traducere folosind sinonimele cuvintelor
	- `ArrayList translateSentences(String sentence, String fromLanguage, String toLanguage)`
	- Întoarce traducerea propoziției sentence din limba **fromLanguage** în limba **toLanguage**
	- În cazul în care nu există **3 variante** de traducere a propoziției se vor furniza doar variantele posibile
9. Metodă pentru întoarcerea definițiilor și sinonimelor unui cuvânt
	-  `ArrayList getDefinitionsForWord(String word, String language)`
	- Definițiile sunt sortate crescător după anul de apariție al dicționarului
10. Metodă pentru exportarea unui dicționar în format JSON
	- `void exportDictionary(String language)`
	- Se va exporta doar partea din structura de date ce ține de limba primită ca parametru și se vor scrie informațiile într-un fișier
	- Cuvintele din JSON sunt ordonate alfabetic, iar definițiile sunt ordonate după anul de apariție al dicționarului

## Detalii despre implementare

### Word  
Clasa principala. Contine informatii despre cuvant, conform formatului specificat in .json fisier. Contine constructor principal si un constructor auxiliar care va completa doar numele, folosit in cautarea unui cuvant in lista. Pe langa asta mai are gettere si settere principale  
  
1. **getSynonyms** va intoarce o lista de sinonime extrasa din definitiile cuvantului.  
2. **equals** verifica egalitatea a doua cuvinte. Daca singularul/pluralul cuvantului primit ca parametru contine cuvantul celalalt(care a fost creat cu constructorul auxiliar) atunci aceste cuvinte sunt identice. Se verifica apoi daca au campul word egale, iar apoi daca campul word al cuvantului this e egal cu campul word_en al cuvantului parametru(asta in caz cand cuvantul care a fost creat cu constructorul auxiliar are pe post de word cuvantul in engleza(spre exemplu cand se cere traducerea din/in engleza))  
3. **compareTo** folosit la sortarea cuvintelor.  
  
### Definition  
Clasa principala. ontine informatii despre cuvant, conform formatului specificat in .json fisier. Contine constructor principal si un constructor auxiliar care va completa doar campul dict. Pe langa asta gettere si settere  
a campurilor principale  
  
1. **equals** la fel ca in cazul word, doar ca deja va compara campurile dict, dictType si year. Daca toate aceste campuri vor fi egale - avem dictionare identice deci va trebui sa verificam in exterior campul **text**.  
2. **compareTo** face compararea a doua dictionare utilizand campul year. Asta se foloseste la sortarea dictionarelor.  
  
### Administration  
Clasa de administrare a dictionarelor. Variabile membru:  
  
- **Map<String, ArrayList\<Word\>\> langs** - Map care va stoca dictionarele. Cheia String - limba, ArrayList\<Word\> - lista de cuvinte a limbii date.  
- **Random r** - variabila de tip Random, folosita la randomizarea variantelor de traducere a propozitiilor  
  
1. **searchWord** - cauta cuvantul din limba ceruta in colectia noastra de stocare. Verifica existenta limbii, dupa care extrage cuvintele de la cheia gasita, cauta cuvantul in lista. Daca lista nu contine cuvantul cuvantul cautat returnam null, in caz contrar intoarcem referinta la obiectul Word din lista noastra.  
2. **refreshLanguageDictionary** metoda care face refresh la dictionarul unei limbi cerute. Primeste lista de cuvinte existente, lista de cuvinte noi care trebuiesc adaugate si limba. Parcurce cuvintele noi si le cauta in lista de cuvinte existente. Daca s-a gasit, gasim cuvantul existent, extragem definitiile sale si le facem refresh cu metoda **checkWordForDefinitionRefresh**. Daca insa cuvantul nou nu exista in cuvintele noastre existente, simplu il adaugam.  
3. **sortWordsAndDefinitions** sorteaza toate cuvintele si definitiile lor din baza de data(din colectia de dictionare)  
4. **getSentenceFromArray** primeste un String[] care sunt un array de cuvinte si le concateneaza intr-un singur String. La sfarsit se elimina spatiul de pe ultima pozitie a string-ului(care se adauga in for)  
5. **checkWordForDefinitionRefresh** primeste un obiect Word si o lista de definitii care trebuie adaugata la cuvant. Variabila locala uptated e defapt un flag care va semnala ca definitiile cuvantului au fost update. Se parcurge definitiile din lista primita ca parametru si se verifica, daca nu exista in cuvantul nostru, se adauga, daca exista, verificam campul text. Daca cuvantul nostru existent nu contine vre-o definitie din definitia noua - se adauga si se seteaza flagul. Returneaza true - cuvantul s-a modificat, false - invers.  
6. **addWord** daca limba primita ca parametru nu exista, se adauga la cheia language o lista noua, in care se adauga cuvantul nou. Apoi, se verifica daca cuvantul nu cumva exista - daca exista -> checkWordForDefinitionRefresh(cuvantGasit, cuvantDeAdaugat.getDefinitions()). In caz ca cuvantul nu exista -> adauga la key(language) word.  
7. **addDefinitionForWord** se verifica existenta limbii, dupa care se verifica daca cuvantul exista. Daca cuvantul exista, verificam daca in definitiile sale nu cumva exista definitia care vrem sa o adaugam. Daca nu existam, ii facem un simplu add, iar daca exista, trebuie sa verificam pentru refresh. Pentru asta cream o lista auxiliara in care va fi un singur element(definitia primita ca parametru) si apelam checkWordForDefinitionRefresh. Crearea unei liste a fost facut cu scopul de a putea utiliza metoda specificata, deoarece cere ca al doilea parametru o lista de Definition.  
8. **translateWord** se cauta cuvantul in limbile fromLanguage si toLanguage. In continuare urmeaza cateva scenarii:  
   - Traducere en -> !en : daca nu exista toLanguage/cuvantul in toLanguage -> return word. Caz contrar -> se intoarce cuvantul din toLanguage(nici o forma de plural/singular deoarece traducerea din en nu presupune asta fiindca en e doar pentru a face legaturile intre limbi)  
   - Traducere !en -> en : daca nu exista fromLanguage/cuvantul in fromLanguage -> return word. Caz contrar     -> se intoarce forma de engleza a cuvantului fromLanguage.  
   - Traducere !en -> !en : daca nu exista cel putin una din limbi -> return word. Daca nu exista cuvantul in fromLanguage -> return word. Se cauta cuvantul in toLanguage cu ajutorul formei de engleza a cuvantului gasit in fromLanguage. In continuare se verifica daca cuvantul in fromLanguage e la plural/singular pentru a putea sa il traducem la fel in forma respectiva.  
9. **translateSentence** mai intai se prelucreaza propozitia, se sterg toate caracterele diferite de litere, white-spaces si se trec toate literele la lowercase. Se face split dupa spatiu pentru a extrage cuvintele. Se creeaza un array auxiliar de String care va stoca cuvintele traduse. Pentru fiecare cuvant din propozitie se apeleaza translateWord, traducerea lui se stocheaza in array auxiliar. La sfarsit, se apeleaza **getSentenceFromArray(translated)** care ne va returna propozitia tradusa din array-ul de cuvinte traduse.  
10. **translateSentences** se apeleaza translateSentence si obtinem propozitia tradusa. Facem split ca sa obtinem fiecare cuvant in parte, dupa care intr-o lista auxiliara vom stoca obiecte de tip Word pentru fiecare cuvantu din propozitia tradusa. Daca cuvantul nu a fost tradus, adica nu a fost gasit in limba toLanguage, se va adauga in lista un obiect auxiliar, care va avea unele campuri null. In continuare se creeaza o lista de lista wordVersions, care defapt va fi gen o lista de versiuni pentru fiecare cuvant. se parcurge lista de obiecte create anterior si se verifica. Daca e verb sau cuvant creat auxiliar(care nu a putut fi tradus) atunci versiunea sa unica va fi cea care a fost furnizata de translateSentence. In caz contrar apar alte verificari: daca cuvantul furnizat e la plural atunci la fel, asta e unica sa varianta fiindca chiar daca are sinonime, ele vor fi la singular si nu pot fi considerate drept variante alternative de traducere(caini nu putem inlocui cu synonym latrator). In caz ca cuvantul este la singular, atunci variantele sale alternative de traducere vor fi singularul si sinonimele sale. In continuare, res -> lista de propozitii, storage -> lista ce va salva cuvintele dintr-o propozitie. Se intra intr-o bucla, i<3 (atatea variante se cer de furnizat). Se parcurge lista de versiuni a unui cuvant. Se randomizeaza un index pentru a extrage o varianta random a unui cuvant. Se verifica, daca nu e unica varianta -> se sterge varianta de la indexul randomizat, iar in lista storage se adauga varianta data. Dupa ce au fost parcurse versiunile, se creeaza o propozitia cu **getSentenceFromArray** si se adauga la lista finala de variante de traducere a propozitiei. Se face clear la lista de storage si se merge la pasul urmator.  
11. **getDefinitionForWord** se face intai sort la toata colectia noastra (conform cerintei). Daca nu exista limba sau cuvantul se intoarce un ArrayList gol. In caz contrar, se  cauta cuvantul si se intoarce cuvantulGasit.getDefinitions().  
12. **exportDictionary** se face in primul rand sort la toata colectia noastra. Daca nu exista limba ceruta se iese din functie. Se creeaza un fisier cu pathname "dirs_out" care de fapt va fi director si este folosit pentru a verifica existenta directorului dirs_out in care se va face export dictionarului. Daca acel director nu exista, el se creeaza. Dupa care, se creeaza dictionarul in acel director cu numele language_dict.json .Se foloseste GsonBuilder pentru a putea seta format pretty a json si pentru a dezactiva escaparea HTML.  
  
### Init  
Aici de fapt e main-ul de unde incepe citirea si operarea cu dictionare. La citire se presupune ca directorul de unde se citesc dictionarele exista si acolo se afla doar fisiere valide(format specificat si doar .json).
  
### Test  
Clasa unde se fac testarile pentru fiecare metoda. Ca variabila membru avem Administration admin pentru a avea acces la metode. Testul pentru metoda getDefinitionForWord este comentat pentru a nu umple consola fiindca se vor afisa definitiile in format `.json`.

### Copyright 
Drepturile asupra enuntului temei, fisierelor ro_dict.json si fr_dict.json sunt rezervate de catre echipa POO 2021, ACS, UPB.
