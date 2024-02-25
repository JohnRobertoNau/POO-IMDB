I.	Clase si interfete
II.	Design Patterns
III.	Flow
IV.	Interfata Grafica


I.	In clasele Actor, User, Request si Production am folosit Jackson pentru a citi datele din fisierul Json, mai exact in constructorii acestor clase.

La crearea unor noi requesturi, data requestului creat este completata automat cu data si momentul in care cererea a fost completata, pentru a face procesul de requesting sa fie mai practic.

Requesturile se rezolva manual. Astfel, un Admin sau un Contributor isi pot vedea lista de Request-uri  (sau RequestHolder-ul daca este Admin), trecand pe rand prin fiecare request, fiind intreebat daca l-a rezolvat, daca il va rezolva, sau daca refuza. Daca alege optiunea „solve later”, va fi redirectionat la meniu pentru a rezolva manual cererea, iar dupa aceea va trebui sa intre in lista de cereri pentru a marca cererea ca rezolvata, aceasta fiind stearsa din lista de cereri, utilizatorul primind experienta pentru ca i s-a rezolvat cererea. De asemenea , cu ajutorul metodelor solvedRequest si rejectRequest, se ataseaza utilizatorii care au facut cererea ca observatori, acestia primind o notificare.

In clasa Loader sunt declarate 4 metode care se ocupa cu incarcarea datelor din fisierele json, cu ajutorul ObjectMapper-ului.

Actor: clasa contine campuri pentru nume, o lista de obiecte Performance, fiind clasa interna a clasei Actor, si o biografie. Prin intermediul constructorului se citesc datele din fisierul json. Clasa contin diferite metode folosite in aplicatie.

Production: mosteneste Subject si implementeaza comparable.
Pe langa declararile campurilor, o metoda specifica este calculateAverageRating(), care calculeaza media ratingurilor pentru productii. In metodele createReview si removeReview atasam userii ca observatori pentru a primi o notificare de confirmare ca recenzia lor a fost publicata, iar averageRating-ul pentru acea productie este actualizat. Prin json, la inceputul codului separam doua „subtipuri” de production, si anume movie si series.

User: contine subclasa information, care se construieste cu ajutorul unui builder. Pe langa asta, metode semnificative mai sunt isUniqueUsername, care la crearea unui nou user verifica daca usernameul este folosit, si metoda createUser, care genereaza un username in functie de nume si prenume, iar daca nu este unic, se mai adauga cifre la sfarsit. userGetProductionsContributions si userGetActorsContributions sunt folosite pentru a identifica actors si productions contribution-ul pentru un user, pentru ca in cazul in care acesta va fi sters, cererile pentru acele productii/actorii sa fie redirectionate catre un Admin din lista de admini.

RequestStrategy, RatingStrategy si AddingInTheSystemStrategy:
Sunt clase care actualizeaza experienta utilizatorului in functie de actiunile acestora:
	+5p pentru adaugarea unei recenzii
	+10p daca i se rezolva o cerere
	+15p daca adauga in system un actor sau o productie
 


II.	Singleton Pattern: L-am folosit in clasa IMDB, mai exact in constructor, pentru a avea o instanta unica a acestei clase.

Builder Pattern: L-am folosit pentru construirea clasei Information. In interiorul acestei clase existo o clasa InformationBuilder care reprezinta builder pattern-ul nostru, pattern utilizat in incarcarea datelor din fisierele json si mai tarziu, in user factory.

Factory Pattern: In clasa UserFactory am folosit acest pattern pentru a construi fiecare utilizator in functie de tipul acestuia (Account Type).

Observer Pattern: Pentru a implementa acest design pattern am folosit clasa abstracta Subject si interfata Observer. In clasa, metoda notifyObservers notifica toti observatorii atasati. In cazul nostru, userii care trebuie sa primeasca notificari.

Strategy Pattern: Am folosit o interfata ExperienceStrategy care contine metoda calculateExperience. Apoi in clasele RequestStrategy, RatingStrategy si AddingInTheSystemStrategy, care implementeaza aceasta interfata, am folosit metoda de calcul a experientei pentru fiecare caz.



III.	Flow-ul aplicatiei. In clasa IMDB, in metoda run utilizatorul este intrebat sub ce forma trebuie sa se deschida aplicatia (Terminal sau Interfata Grafica), incarcandu-se mai intai datele din fisierele json. 

Am folosit un label „loggingInLoop:” pentru ca in cazul in care utilizatorul vrea sa se deconecteze, sa ne intoarcem la etapa de logare, fara a inchide aplicatia, pastrand toate modificarile facute in sistem pana in acel moment. Cu ajutorul functiilor verifyEmail si verifyPassword se verifica corectitudinea datelor de logare. In caz ca se reuseste logarea, variabila isAuthenticated este setata la true, pentru a se opri loop-ul de logare. Apoi, in functie de tipul utilizatorului logat, se afiseaza meniul. Pentru indeplinirea sarcinilor din meniu, m-am folosit de niste metode ajutatoare, acestea fiind vizibile dupa finalul metodei run.

Corectitudinea comenzilor este asigurata in functia scanningNextInt(), care verifica daca s-a introdus un int si se executa comanda scanningNextLine() pentru a consuma newline-ul. De asemena, pe parcursul flow-ului se mai verifica anumite exceptii, atat cat la crearea de noi obiecte Information(daca sunt nule, scurte sau daca nu respecta formatul unor credentiale) sau daca se introduce un int care nu are legatura cu activarea unei comenzi. 




IV.	Daca utilizatorul apasa 2, se deschide interfata grafica, mai exact clasa LoginGUI. Am folosit o imagine de fundal, iar datele de logare le-am introdus in centrul acesteia, pentru a arata mai bine. In clasa LoginGUI am folosit un Jframe, un JtextField pentru a se introduce emailul, JPasswordField pentru a se introduce parola, un JButton de login si un JLabel pentru un mesaj de eroare. Pe langa asta am mai declarat un obiect static de tip User, pentru a avea acces la toate informatiile acestui obiect in timpul rularii interfetei grafice.

Metode performLogin() returneaza userul care s-a logat, cu ajutorul metodei din clasa IMDB, findUserByEmail(), si deschide fereastra Main, adica o clasa MainPage(frame), unde de frame m-am folosit in constructorul clasei MainPage pentru a inchide pagina initiala de login.

In clasa MainPage am folosit doua JTextField-uri pentru genreField si ratingField, pentru ca utilizatorul sa poata filtra productiile in functie de gen si numarul de ratinguri.

La deschiderea acestei paginii se afiseaza initial titlul unor 5 productii recomandate. Aceste 5 productii de adauga random din sistem in JTextArea-ul recoText.

Daca utilizatorul alege sa filtreze productiile, in locul recomandarilor vor aparea productiile. 

Daca utilizatorul alege sa vada toti actorii, apasand butonul „View Actors”, se vor afisa toti actorii sortati dupa nume, in recoText, adica in locul unde au fost initial recomandarile.

recoText devine ulterior JTextArea-ul unde se vor afisa toate rezultatele in functie de actiunile utilizatorului.

Daca utilizatorul introduce un nume de actor sau productie in josul paginii si apoi apasa butonul „Search movie/series/actors”, rezultatele gasite vor fi afisate in recoText.

Daca utilizatorul apasa butonul „View Notifications”, notificarile vor fi afisate in recoText

In stanga acestor optiuni din josul paginii, se poate vedea usernameul utilizatorului logat si experienta acestuia. 

Daca utilizatorul apasa Menu, se va deschide o pagina corespondenta unei clase, care variaza in functie de tipul utilizatorului. Adica daca un utilizator de tip Admin apasa butonul „Menu”, se va deschide pagina generata de MenuAdmin.

Mentionez ca paginile MenuAdmin, MenuContributor si MenuRegular sunt goale, nu am avut timp sa continui implementarea lor.

Prin urmare, in functia MainPage sunt toate optiunile comune pe care le poate face un Regular Admin si un Contributor, (mai putin Adăugarea/S,tergerea unei product, ii/actor din lista de favorite) si anume:
-	Vizualizarea detaliilor tuturor product, iilor din sistem (putand fi filtrate dupa preferinte)
-	Vizualizarea detaliilor tuturor actorilor din sistem (acestia fiind afisati sortati)
-	Vizualizarea notificărilor primite
-	Logout. Daca utilizatorul apasa butonul Logout, acesta este trimis la pagina de login, modificarile putand fi salvate pana in momentul inchiderii aplicatiei (daca s-ar fi implementat meniul fiecarui utilizator =]]).
Am mai adaugat in folderul resources un folder ‚images’ care include doua imagina folosite in interfata grafica.
