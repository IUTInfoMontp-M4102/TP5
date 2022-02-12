# TD5 - Communication à l'aide de boîtes aux lettres

Lien GitHub Classroom pour [faire un fork privé du TP](https://classroom.github.com/a/4sXj4Mly).

Le but de ce TD est de mettre en place un système permettant à des processus différents de communiquer. La solution 
proposée ici est d’utiliser une *boîte aux lettres* dans laquelle les processus peuvent déposer des messages ou 
lire les messages déposés.

Dans un premier temps, pour des raisons de simplicité, nous allons ici faire communiquer des *threads* d'un même processus, mais nous verrons plus tard comment on peut utiliser ce mécanisme avec des *sockets* pour faire communiquer 
des processus différents (éventuellement même sur des machines différentes à travers un réseau).

## Exercice 1 - Cas simple

Dans un premier temps, on s’intéresse à la réalisation d’une boîte aux lettres ne pouvant contenir qu’un seul message à la fois. Cette boîte aux lettres est utilisée entre un client et un serveur : le client dépose un message pour le serveur (une requête) puis le serveur lit le message. Dans ce premier exemple, le serveur va se contenter d’afficher à l’écran le message reçu. Il n’est donc pas nécessaire de prévoir de mécanisme pour renvoyer des messages du serveur vers le client.

On utilise deux sémaphores : un sémaphore `sDepot` pour indiquer aux clients que la boîte aux lettres est libre, et un sémaphore `sRetrait` pour indiquer au serveur qu’un message a été déposé dans la boîte aux lettres.

On va créer une classe `BalSimple` comportant une méthode `deposeRequete` qui attend que la boîte aux lettres soit libre puis y dépose le message passé en argument, et une méthode `retireRequete` qui attend que la boîte aux lettres contienne un message puis vide la boîte aux lettres et renvoie le message qui s’y trouvait.

1. Complétez la classe `BalSimple` avec son constructeur et les deux méthodes `deposeRequete` et `retireRequete`.

Par la suite, les sémaphores `sDepot` et `sRetrait` ne doivent être modifiés que par l'intermédiaire des méthodes `deposeRequete` et `retireRequete`.

2. Complétez la classe `Client` qui réalise une tâche qui dépose un unique message dans une boîte aux lettres.
    
    Les clients seront créés avec un numéro `id` comme dans le TD précédent et le message envoyé contiendra le numéro du client qui l'a envoyé.

3. Complétez la classe `Serveur` qui implémente une tâche qui attend qu’un message soit déposé dans la boîte aux lettres et affiche ce message.
   
    Les serveurs sont également identifiés et l’affichage du message doit faire apparaître le nom du serveur qui a reçu le message.

4. Écrivez la classe principale `Ex1Main` de votre application qui exécute un `Client` et un `Serveur` fonctionnant sur la même boîte aux lettres (sinon ce n’est pas très intéressant...).
   
   Vérifiez que le serveur reçoit et affiche le message envoyé par le client.

5. Modifiez la classe `Client` pour que chaque client dépose maintenant 10 requêtes successives dans la boîte aux lettres, en ajoutant un délai aléatoire entre deux requêtes.

6. Modifiez la classe `Serveur` pour que la tâche exécute une boucle infinie de lectures de messages postés dans la boîte aux lettres. Pour simuler le temps de traitement d’une requête, ajoutez un délai aléatoire compris entre 100 et 300 ms après avoir récupéré un message dans la boîte aux lettres.

7. Modifiez le programme principal pour qu’il lance une tâche `Serveur` et trois tâches `Client`.

8. Quand le programme se termine-t-il ?

**Bonus :** Réfléchissez aux différentes manières que l'on pourrait utiliser pour arrêter le serveur lorsque toutes les requêtes des clients ont été reçues.


# Exercice 2 - Question, réponse

On considère toujours une boîte aux lettres unique ne pouvant contenir qu’un seul message mais cette fois-ci on veut que le serveur envoie une réponse au client après avoir traité sa requête.

Le déroulement est donc le suivant :
- lorsque la boîte aux lettres est disponible, un client peut y déposer une requête. Une fois qu'il a déposé sa requête il que la boîte aux lettres contienne une réponse du serveur ;
- lorsque la boîte aux lettres contient une requête, le serveur peut lire ce message. Tant que le serveur n’a pas répondu, la boîte aux lettres est en attente de réponse ;
- lorsque le serveur a fini de préparer la réponse à la requête du client, il dépose cette réponse dans la boîte aux lettres ;
- lorsque la boîte aux lettres contient une réponse, le client qui avait placé la requête peut lire la réponse, ce qui a pour effet de libérer la boîte aux lettres (elle revient dans l'état initial où les clients peuvent déposer une requête).

1. Combien d’états différents utilise-t-on pour la boîte aux lettres ? Combien de sémaphores va-t-on devoir utiliser ?

Comme dans l’exercice précédent, les sémaphores seront gérés par les méthodes de la boîte aux lettres.

2. Complétez la classe `BalQR` disposant des méthodes suivantes :
- `deposeRequete(String mess)` qui permet à un client de déposer un message ;
- `retireRequete()` qui permet à un serveur de lire le message déposé par un client. Cette méthode bloque la boîte aux lettres jusqu’à ce qu’elle reçoive la réponse du serveur ;
- `deposeReponse(String mess)` qui permet au serveur qui avait reçu la requête du client de déposer la réponse à cette requête ;
- `retireReponse()` qui permet au client qui avait envoyé la requête de lire la réponse, et libère la boîte aux lettres.

3. Écrivez la classe `ClientQR` qui implémente une tâche qui dépose successivement 6 requêtes dans la boîte aux lettres. Après chaque dépot d’une requête, il faut récupérer et afficher la réponse du serveur. Ajoutez un délai entre la réception d’une réponse et l’envoi de la requête suivante.

4. Écrivez la classe `ServeurQR` qui exécute une boucle infinie dont chaque itération consiste à lire une requête postée dans la boîte aux lettres, attendre un délai aléatoire pour simuler le temps de traitement, puis déposer la réponse à la requête. Dans cet exercice, la réponse à un message est simplement le message préfixé de la chaîne « Serveur *i*, Réponse: » où *i* est le numéro du serveur.

5. Testez votre programme en ne lançant initialement qu’un seul `ClientQR`, et un seul `ServeurQR`. Puis lancez deux `ServeurQR` et trois `ClientQR`.


# Exercice 3 - Emplacements multiples

On revient à la situation de l’exercice 1 (pas de réponse du serveur), mais on veut ajouter des emplacements à la boîte aux lettres pour qu’elle puisse contenir plusieurs messages à la fois.

Afin que les requêtes déposées dans la boîte aux lettres soient traitées dans leur ordre d’arrivée (FIFO), on utilise deux indices :
- `indiceDepot` désigne l'indice de la case du tableau dans laquelle on placera la prochaine requête ;
- `indiceRetrait` désigne l'indide de la case du tableau où se trouve la prochaine requête à traiter (la plus ancienne).

Ces deux indices sont initialisés à 0 et incrémentés à chaque fois que l’opération à laquelle ils correspondent est effectuée. Le tableau est considéré comme circulaire c’est-à-dire que l’incrémentation est faite modulo le nombre de cases du tableau :
```java
this.indiceDepot = (this.indiceDepot + 1) % this.nb
```

Les sémaphores `sDepot` et `sRetrait` ont une signification légèrement plus complexe qu’avant puisqu’ils représentent respectivement le nombre de cases vides de la boîte aux lettres et le nombre de requêtes en attente de traitement.

1. Écrivez les méthodes `deposeRequete` et `retireRequete` de la classe `BalTable` en utilisant les sémaphores et les indices.

2. En reprenant le code des classes `Client` et `Serveur` de l'exercice 1, complétez les classes `Client` et `Serveur` pour que les tâches travaillent sur une boîte aux lettres de type `BalTable`.

3. Écrivez la classe `Ex3Main` pour que le programme exécute 3 tâches `Client` et 1 tâche `Serveur`.

 1. Identifiez les problèmes de synchronisation qui peuvent se produire si deux requêtes sont déposées simultanément dans la boîte aux lettres. Résolvez ces problèmes en ajoutant des verrous là où c’est nécessaire.
