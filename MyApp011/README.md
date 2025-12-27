#TUTORIÁL: Jednoduchá Android aplikace s formulářem, fotkou a detailem

V Android aplikace v **Kotlinu**, která podporuje:
- nahrát obrázek z galerie
- vyplnit osobní údaje (jméno, příjmení, pohlaví, věk)  
- uložit formulář
- zobrazit předchozí uložené záznamy
- zobrazit detail každého uloženého záznamu  

---

## 1) Vytvoření nového projektu

1. Otevřít **Android Studio**  
2. Založit nový projekt s **Empty Views Activity**
3. Zvolit libovolný název projektu
4. Jazyk: **Kotlin**  
5. Minimální SDK: **API 24 (Android 7.0)**  
6. Dokončit vytvoření projektu.

---

## 2) Zapnout ViewBinding

K lepšímu přístupu k prvkům layoutu bez `findViewById`, zapneme **View Binding**.

Ve složce `build.gradle (Module: app)` přidáme následujících pár řádků.

```gradle
android {
    ...
    buildFeatures {
        viewBinding true
    }
}
```

Po přidání výše uvedeného kódu zapneme `Sync project with Gradle files` (Ikonka slona v pravé horní části aplikace)

Nyní je možné začít samotný vývoj aplikace.

## 3) Vytvořit layouty pro 2 různé zobrazení

První layout je už vytvořený (`app -> res -> layout -> activity_main.xml`)
Do stejné složky zároveň vytvoříme layout s názvem `activity_detail.xml`

Tato aplikace využívá v obou složkách LinearLayout, ale využití ConstraintLayout-u je zde také možné.

Layout musí obsahovat - input pro jméno, input pro příjmení a input pro věk, který bude akceptovat pouze číslo. Dále zde musí být tlačítko pro uložení, nahrání fotky a náhodné zvolení věku. Posledním prvkem formuláře je sada tří radio tlačítek, kde se volí pohlaví. Pod samotným formulářem se budou zobrazovat již odeslaná data, kde bude tlačítko pro detail. 

Po stishnutí tlačítka detailu se uživatel přesune na další stránku, kde se zobrazí data o konkrétním uživately, na jejž detail klikl. 

Pro konkrétní kód, který byl využit v tomto projektu je možné navštvívit [složku layoutů](app/src/main/res/layout).

## 4) Logika aplikace

V této kapitole je popsána logika fungování aplikace, tedy způsob, jakým aplikace reaguje na akce uživatele a jak spolu jednotlivé části aplikace spolupracují.

Po spuštění aplikace se připraví proměnná pro uložení vybraného obrázku a nastaví se mechanismus, který umožňuje otevřít galerii zařízení a vybrat z ní obrázek. Uživatel může pomocí tlačítka Nahrát otevřít galerii a zvolit fotografii, která se následně uloží do aplikace a zobrazí se v náhledu na hlavní obrazovce. Pokud uživatel žádný obrázek nevybere, aplikace s tímto stavem korektně pracuje a nedojde k chybě.

Součástí formuláře je také tlačítko Random, které slouží k automatickému vygenerování věku. Věk je generován náhodně v předem daném rozsahu a výsledná hodnota se automaticky vloží do pole pro zadání věku. Uživatel má možnost tuto hodnotu kdykoliv ručně upravit.

Po kliknutí na tlačítko Uložit aplikace nejprve zkontroluje, zda jsou vyplněna všechna povinná pole formuláře. Pokud některý z údajů chybí, aplikace na tuto skutečnost upozorní uživatele. V případě, že jsou všechna pole správně vyplněna, dojde ke zpracování dat – načtou se hodnoty jména, příjmení, věku a zvoleného pohlaví a k těmto údajům se připojí vybraný obrázek.

Po úspěšném uložení formuláře se nový záznam dynamicky přidá přímo na hlavní obrazovku aplikace. Tento záznam obsahuje náhled fotografie, textové informace o uživateli a tlačítko Detail, pomocí kterého lze zobrazit podrobné informace. Tímto způsobem je možné v aplikaci zobrazovat více uložených záznamů bez nutnosti použití databáze.

Po kliknutí na tlačítko Detail se otevře nová obrazovka aplikace (DetailActivity), do které se předají všechny potřebné údaje – jméno, příjmení, věk, pohlaví a fotografie. Tyto informace se na detailní obrazovce přehledně zobrazí uživateli. Detail slouží pouze ke čtení informací a jejich přehlednému zobrazení.

Na detailní obrazovce se nachází tlačítko Zpět, pomocí kterého se uživatel vrátí zpět na hlavní obrazovku aplikace. Při návratu nedochází ke ztrátě žádných uložených záznamů.

Po úspěšném uložení formuláře aplikace automaticky vyčistí vstupní pole, odstraní zvolený obrázek a resetuje výběr pohlaví na výchozí hodnotu. Tím je aplikace připravena k zadání dalšího záznamu.

Po každém úspěšném uložení dat aplikace zobrazí krátké informační hlášení, které slouží jako zpětná vazba pro uživatele a potvrzuje, že byl formulář úspěšně uložen.

Pro konkrétní kód je možné navštívit [tuto složku](app/src/main/java/com/example/myapp010brandomapp).
