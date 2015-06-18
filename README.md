#Super Mercado

## Execução

## Compilação

## Protocolo
O protocolo usado para se comunicar com o servidor é parecido com HTTP, porém mais simples. O modelo de uma requisição/resposta é o seguinte:
```
<COMANDO><\n>
[<VARIÁVEL>:{CONTEÚDO}<\n>]
<\n>
```
`<COMANDO>` e `<VARIÁVEL>`: São identificadores, e devem ser compostos apenas de letras (maiúsculas ou minúsculas).


`{CONTEÚDO}`: Pode ser qualquer sequencia de caractere ASCII válidos, exeto caracteres de controle. **Nota:** Espaços após os ':' são interpretados como parte da variável!.

`<\n>`: Representa uma quebra de linha.
A parte entre colchetes pode ser repetida 0 ou mais vezes.

As respostas do servidor seguem o mesmo modelo das requisições, porém além do cabeçalho (delimitado pelas duas quebras de linhas seguidas) as respostas podem conter mais informações de acordo com o comando que gerou a resposta.

### Comandos válidos:
#### LOGIN
**Requisição:**
```
LOGIN
user:nome_de_exemplo
pass:s3nh4_s3cr374

```
**Respostas:**
```
OK
token:ExEmPlOeXeMpLoExEmPlO+==

```
Quando o login é efetuado com sucesso o token identifica a sessão do usuário. Todas as ações que requerem login precisam de um token válido para serem efetuadas.

```
ERRO

Usuário e/ou senha inválidos.
```
Quando o login falha.

### LOGOUT
**Requisição:**
```
LOGOUT
token:ExEmPlOeXeMpLoExEmPlO+==

```

**Resposta:**
```
OK

```
A resposta de um LOGOUT é sempre OK, mesmo que o token não exista ou já tenha feito logout.
