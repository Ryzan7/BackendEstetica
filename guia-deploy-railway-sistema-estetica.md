# 🚀 Guia de Deploy Railway - Sistema Estética

## 📋 Pré-requisitos
- [ ] Conta Railway criada ([railway.app](https://railway.app))
- [ ] Railway CLI instalado (`npm install -g @railway/cli`)
- [ ] Git configurado no projeto

---

## 🔧 Passo a Passo - Deploy

### 1. **Login Railway (modo browserless)**
```bash
# Inicia o login sem abrir navegador
railway login --browserless
```

O terminal exibirá um **código temporário**.  
Copie esse código, abra [railway.app/cli-login](https://railway.app/cli-login), cole no campo solicitado e confirme.  
Depois disso, o terminal será autenticado.

---

### 2. **Criar ou Vincular Projeto**
```bash
# Dentro da pasta do projeto
railway init
```
Esse comando cria ou conecta ao projeto Railway existente.

---

### 3. **Configurar Database (PostgreSQL)**
> ⚠️ A criação de banco agora é feita **no painel web**.

1. Execute:
   ```bash
   railway open
   ```
2. No navegador, vá em **New → Database → PostgreSQL**.  
3. O Railway criará automaticamente a variável de ambiente:
   ```
   DATABASE_URL=postgresql://user:password@host:port/dbname
   ```

---

### 4. **Deploy Backend (API)**
```bash
# Navegar para backend
cd api-estetica

# Primeiro deploy
railway up
```

Após o deploy, configure as variáveis de ambiente:

```bash
railway variables set SPRING_PROFILES_ACTIVE=production
railway variables set DATABASE_URL=${{ DATABASE_URL }}
railway variables set JWT_SECRET="minha-chave-secreta-super-segura-para-jwt-2024"
railway variables set PORT=8080
```

> ✅ O Railway já exporta `DATABASE_URL`, então você pode usá-la diretamente no backend em vez de quebrar em host/port/user/password.

---

### 5. **Deploy Frontend**
```bash
# Navegar para frontend
cd ../cest-app-estetica

# Deploy
railway up
```

Configurar a variável de ambiente apontando para o backend:
```bash
railway variables set VITE_API_URL=https://[BACKEND_URL].railway.app
```

---

## 🔐 Variáveis de Ambiente

### **Backend (API)**
```
SPRING_PROFILES_ACTIVE=production
DATABASE_URL=${{ DATABASE_URL }}
JWT_SECRET=minha-chave-secreta-super-segura-para-jwt-2024
PORT=8080
```

### **Frontend**
```
VITE_API_URL=https://[SEU-BACKEND-URL].railway.app
```

---

## 🌐 URLs Finais
- **Backend**: `https://[projeto]-backend.railway.app`
- **Frontend**: `https://[projeto]-frontend.railway.app`
- **Database**: variável `DATABASE_URL` (interno Railway)

---

## 🔍 Verificação
1. **Backend Health**:  
   `GET https://[backend-url]/api-estetica/actuator/health`
2. **Frontend Health**:  
   `GET https://[frontend-url]/health`
3. **API Test**:  
   `POST https://[backend-url]/api-estetica/api/auth/login`

---

## 🐛 Troubleshooting

### **Erro de CORS**
- Verificar `VITE_API_URL` no frontend  
- Confirmar CORS no backend (`application-production.yaml`)  

### **Erro de Database**
- Conferir se o backend usa `DATABASE_URL`  
- Validar conexão DB nos logs  

### **Build Failure**
- Ver logs:  
  ```bash
  railway logs
  ```  
- Conferir `railway.toml`  

---

## 📝 Comandos Úteis
```bash
# Ver logs
railway logs

# Listar variáveis
railway variables

# Redeploy rápido
railway up --detach

# Status do projeto
railway status
```
