# TD5 - Communication à l'aide de boîtes aux lettres

Lien GitHub Classroom pour [faire un fork privé du TP]().

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