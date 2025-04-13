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

## 💻 Fonctionnement côté Frontend

### 📦 Technologies requises

- `SockJS` pour gérer la connexion WebSocket avec fallback
- `Stomp.js` pour parler STOMP au-dessus de WebSocket
- Un **token JWT valide** pour l'authentification

  🔍 C'est quoi SockJS ?

   SockJS est une bibliothèque JavaScript qui permet d'établir une connexion WebSocket compatible avec tous les navigateurs, même ceux qui ne supportent pas WebSocket nativement.
   
   Fonctionnement :
   SockJS essaie d’abord d’utiliser `WebSocket`.
   
   Si ce n’est pas possible (navigateur trop ancien, proxy bloquant, etc.), il bascule automatiquement vers une solution de secours comme :
   - `Long polling`,
   - `Streaming HTTP`,
   - `Iframes, etc`.

  🔍 C'est quoi STOMP ?
  
   - STOMP donne une structure à la communication WebSocket
   - Tu peux envoyer, écouter, s’abonner, se désabonner
   - Spring a un support intégré pour STOMP
 
   - Exemple de message STOMP
        ```bash
        SEND
        destination:/app/private-message
        content-type:application/json
        { "toAccountId": "bob", "content": "Hello!" }
        
### 🚀 Exemple d’implémentation en JavaScript
   ```html
      <!-- index.html -->
      <script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
      <script src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>
      
      <div id="chat-box" style="height:200px;overflow:auto;border:1px solid #ccc;"></div>
      <input id="message-input" placeholder="Message...">
      <button id="send-button">Envoyer</button>
      
      <script>
      const token = "votre_token_JWT";
      const socket = new SockJS("https://votre-api/ws?Authorization=Bearer " + token);
      const stompClient = Stomp.over(socket);
      
      stompClient.connect({}, () => {
        stompClient.subscribe("/user/private", msg => {
          document.getElementById("chat-box").innerHTML += `<p>${msg.body}</p>`;
        });
      });
      
      document.getElementById("send-button").onclick = () => {
        const msg = document.getElementById("message-input").value;
        stompClient.send("/app/private-message", {}, JSON.stringify({ toAccountId: "admin", message: msg }));
      };
      </script>
   ```

   - Le token JWT est transmis via l’URL au moment du handshake.
   - Le client s’abonne à /user/private pour recevoir les messages ciblés.
   - Il envoie un message à /app/private-message avec le champ toAccountId.
---

---

## ✅ Résumé

- 🔐 Auth via JWT dès le handshake WebSocket
- 👤 Utilisateurs identifiés par `Principal`
- 📬 Envoi à `/app/private-message`
- 📨 Réception à `/user/private`
- 🔒 Messages vraiment privés (seul le bon utilisateur les reçoit)

---
