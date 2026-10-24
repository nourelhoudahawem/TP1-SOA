# TP1 SOA : Modelisation XML et interrogation XPath

**Domaine** : Gestion de scolarite (ecole)

**Matiere** : Architecture SOA et services web, LSI3

## 1. Entites modelisees

| Entite | Attributs | Elements |
|---|---|---|
| etudiant | id, classe, actif | nom, prenom, email, specialite, moyenne |
| enseignant | id | nom, prenom, grade, departement, matiere |
| classe | id | nom, niveau, effectif |
| matiere | id, enseignantRef | nom, coefficient, credits |
| departement | id | nom, chefRef |
| salle | id, type | nom, capacite |

## 2. Relations

- etudiant.classe -> classe.nom
- enseignant.matiere -> matiere.nom
- enseignant.departement -> departement.nom
- matiere.enseignantRef -> enseignant.id (IDREF)
- departement.chefRef -> enseignant.id (IDREF)

## 3. Choix de modelisation

- **Attributs** : pour les identifiants et proprietes courtes (id, actif, type).
- **Elements** :  pour les donnees descriptives (nom, email, moyenne...).
- **ID/IDREF** :  pour les references vers un enseignant.
- **xs:key / xs:keyref** :  pour les references par nom (classe, matiere, departement), car ces valeurs ne sont pas des ID.
- **Types restreints** : moyenne entre 0 et 20, specialite (LSI, RT), niveau (Licence, Master), type de salle (amphi, labo, cours), email par pattern.

## 4. Structure du projet

- `pom.xml`
- `src/main/resources/ecole.xml`
- `src/main/resources/ecole.xsd`
- `src/main/java/com/example/Main.java`

## 5. Validation XML/XSD

Utilisation de `SchemaFactory`, `Schema` et `Validator` (javax.xml.validation).
Resultat : `Le document XML est VALIDE.`
Test invalide : moyenne = 25 -> `Document INVALIDE`.

## 6. Exemples de requetes XPath

| Requete | Explication | Resultat |
|---|---|---|
| `//etudiant[@actif='true']/nom` | Etudiants actifs | Ben Ali, Trabelsi, Mansouri |
| `//etudiant[@classe='3LSI']/prenom` | Prenoms des 3LSI | Aya, Sami, Yassine |
| `//etudiant[moyenne > 14.5]/nom` | Moyenne superieure a 14.5 | Trabelsi |
| `//enseignant[departement='Informatique']/nom` | Enseignants d'informatique | Ben Youssef, Trabelsi |
| `/ecole/matieres/matiere[coefficient >= 3]/nom` | Matieres coefficient >= 3 | Java |
| `//salle[@type='labo']/nom` | Salles labo | Labo Info 1 |
| `sum(//etudiant/moyenne) div count(//etudiant)` | Moyenne generale | 14.25 |

## 7. Execution

Ouvrir le projet dans IntelliJ, lancer `Main.main()`.