# Document de design

Ceci est le document de template pour décrire l'architecture de votre programme. Vous pouvez le modifier à votre guise, mais assurez-vous de répondre à toutes les questions posées.
***Suivant certaines architectures, certaines des questions peuvent ne pas être pertinentes. Dans ce cas, vous pouvez les ignorer.***
Vous pouvez utiliser autant de diagrammes que vous le souhaitez pour expliquer votre architecture.
Nous vous conseillons d'utiliser le logiciel PlantUML pour générer vos diagrammes.

## Schéma général

La classe Client appelle le constructeur CalendarApplication qui lui renverra le calendrier.
CalendarApplication gère les appels et les redirections permettant de construire le bon résultat (gestion des arguments, type de parser(en fonction du type de fichier d'entrée), type de sortie).
Dans un premier temps, elle laisse la gestion des arguments bruts à ArgumentParser, qui construit un CliConfig (entrée, events ou todo, type de sortie et filtre). Puis, grâce à cette configuration, CalendarApplication choisit le bon parseur avec la méthode AbstractParser.chooseParser (soit un fichier local avec ParserFile soit un fichier provenant d'un URL avec ParserURL) afin de produire un Calendar qui contient des Events ou des Todos. 
Dans un second temps, il applique le filtre récupéré dans la configuration client pour ne garder que les éléments requis.
Enfin, il délègue l'affichage via la méthode abstraite displayCalendar de la classe abstraite Output (utilise le bon format de sortie : .txt, .ics, .html) qui écrit sur la sortie ou un fichier si spécifié par l'argument -o


## Utilisation du polymorphisme

Comment utilisez-vous le polymorphisme dans votre programme?

Le polymorphisme se manifeste à plusieurs niveaux :

CalendarComponent est une classe abstraite avec la méthode abstraite printWith(Output O). Event et Todo héritent de CalendarComponent et ils overrident chacun printWith() pour déléguer l'affichage au générateur de sortie approprié. Calendar peut ainsi stocker une liste hétérogène de CalendarComponent et appeler printWith() sans connaître le type concret. De plus, cela permet d'obliger les nouvelles sous-classes de CalendarComponents à gérer leur affichages, permettant une bonne continuité du code et le respect des principes de POO.

On utilise la classe abstraite Output pour diviser l'affichage (header, footer, contenu) en plusieurs format et selon divers composant de calendrier (displayEvent, displayTodo). CalendarApplication utilise ainsi uniquement le type apparent de l'Output sans connaître le type réel (décidé par l'ArgumentParser selon les options passé en ligne de commande (-txt, -ics, -html)).

La classe abstraite AbstractParser permet de définir la méthode abstraite parse. ParserFile et ParserUrl implémentent cette méthode afin de manipuler séparément des fichiers locaux et les URL. La méthode statique chooseParser permet de retourner le bon parser selon le type de fichier d'entrée. CalendarApplication utilise ainsi le type abstrait AbstractParser sans connaître l'origine du parser.


A FAIRE: parler des filtres events et todos avec la méthode abstraite filter, 

## Utilisation de la déléguation

Comment utilisez-vous la délégation dans votre programme?

## Utilisation de l'héritage

Comment utilisez-vous l'héritage dans votre programme?

## Utilisation de la généricité

Comment utilisez-vous la généricité dans votre programme?

## Utilisation des exceptions

Comment utilisez-vous les exceptions dans votre programme?