#Super Mercado

## Execução

## Compilação
Para compilar basta usar o comando `ant`.

## Protocolo
O protocolo usado para comunicação entre servidor e cliente é parecido com cabeçalhos HTTP, porém mais simples:
```
<COMANDO><\n>
[<VARIÁVEL>:{CONTEÚDO}<\n>]
<\n>
```
*A parte entre colchetes pode ser repetida 0 ou mais vezes.*

`<COMANDO>`: Comando que o cliente deseja executar. Deve ser composto apenas de letras maiúsculas ou sublinhados('_'). O primeiro caracter tem que ser uma letra.

`<VARIÁVEL>`: Nome da variável sendo definida. Pode ser composto por qualquer letra (maiúscula ou minúscula), dígitos e sublinhados ('_'). Não pode começar com um dígito.

`{CONTEÚDO}`: Pode ser qualquer sequencia de caractere ASCII válidos, exeto caracteres de controle. **Nota:** Espaços após os ':' são interpretados como parte da variável!.

`<\n>`: Representa uma quebra de linha.

**Nota:** Alguns comandos podem conter dados após o cabeçalho. Os dados e o formato podem variar de acordo com cada comando. 

### Comandos válidos:
#### LOGIN
Quando o login é efetuado com sucesso o token identifica a sessão do usuário. Todas as ações que requerem login precisam de um token válido para serem efetuadas. Quando o usuário faz login novamente ou logout, o token é invalidado.

**Requisição:**
```
LOGIN
user:nome_de_exemplo
pass:s3nh4_s3cr374

```
**Respostas:**

Quando o faz login com sucesso:
```
OK
token:ExEmPlOeXeMpLoExEmPlO+==

```

Quando o login falha:
```
ERRO
msg:Usuário e/ou senha inválidos.

```

### LOGOUT
**Requisição:**
```
LOGOUT
user:nome_de_exemplo
token:ExEmPlOeXeMpLoExEmPlO+==

```

**Resposta:**
A resposta de um LOGOUT é sempre OK, mesmo que o token não exista ou já tenha feito logout.
```
OK

```
