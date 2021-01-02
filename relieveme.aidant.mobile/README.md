Imane MAJDADI 02/01/2020

Copier le dossier relieveme_php dans  local www ou bien vers Server Handi paris 8

créer une nouvelle base de données Mysql en local par example:relievedb  ou bien utiliser le la base sql Server Handi paris 8
Créer la table users
" Query"""
create table users( idint(11) primary key auto_increment, unique_idvarchar(23) not null unique, namevarchar(50) not null, emailvarchar(100) not null unique, encrypted_passwordvarchar(250) not null, otpint(6) NOT NULL, verifiedint(1) NOT NULL DEFAULT '0', created_at datetime DEFAULT NULL );

changer Config.php file for username and password $username = ""; $password = ""; $host = ""; $dbname = ""; 



changer la variable

par exemple sur le serveur Handi
 MAIN_URL = "http://handiman.univ-paris8.fr/~imane/relieveme_php/" 
 
En local 
  MAIN_URL = "http://"Votre Ip"/relieveme_php/" Attent Localhoste ne marche pas(utilisé Ipconfig dans cmd pour trouvé votre ip)