# 🛰️ WebSocket Chat Privé avec Spring Boot & STOMP

Ce projet implémente un système de **messagerie privée en temps réel** entre utilisateurs, en utilisant **WebSocket avec STOMP** et une authentification via **JWT**.

---

## ⚙️ Fonctionnement côté Backend

### 🔌 Connexion WebSocket

Le client se connecte à l'endpoint :

```
/ws?Authorization=Bearer%20<token>
```

> Le token JWT est envoyé en tant que paramètre d’URL au moment du **handshake**.

---

### 🛡️ Handshake Interceptor

Le `HandshakeInterceptor` intercepte la requête de connexion WebSocket et :

- Récupère le token JWT depuis l’URL
- Valide le token
- Extrait l’`accountId` de l'utilisateur
- Stocke l’`accountId` dans la session WebSocket

---

### 🧑‍💼 Handshake Handler

Le `HandshakeHandler` lit l’`accountId` et crée un objet `Principal`. Ce `Principal` est utilisé par Spring pour identifier l’utilisateur tout au long de la session WebSocket.

---

### 📩 Contrôleur de messages

Lorsqu’un client envoie un message à :

```
/app/private-message
```

Le `PrivateChatController` utilise le `SimpMessagingTemplate` pour l’envoyer uniquement à l’utilisateur ciblé via :

```
/user/{toAccountId}/private
```

---

### 📥 Réception ciblée

Le client destinataire doit être abonné à :

```
/user/private
```

Grâce au `Principal`, Spring sait quel utilisateur est connecté, et seul le bon destinataire recevra le message.

---

---

## 🔀 Schéma du flux

```plaintext
Client A
   │
   └──> /ws?Authorization=Bearer token_A
           │
           └──> Intercepteur → Handshake → Principal(accountId = A)

Client B
   │
   └──> /ws?Authorization=Bearer token_B
           │
           └──> Intercepteur → Handshake → Principal(accountId = B)

Client A envoie à B : /app/private-message { toAccountId: B }

Serveur
   └──> Route vers /user/B/private → livré uniquement à B
```

---

## ✅ Résumé

- 🔐 Auth via JWT dès le handshake WebSocket
- 👤 Utilisateurs identifiés par `Principal`
- 📬 Envoi à `/app/private-message`
- 📨 Réception à `/user/private`
- 🔒 Messages vraiment privés (seul le bon utilisateur les reçoit)

---