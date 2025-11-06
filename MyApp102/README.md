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

Po přidání výše uvedeného kódu zapneme "Sync project with Gradle files" (Ikonka slona v pravé horní části aplikace)

Nyní je možné začít samotný vývoj aplikace.


