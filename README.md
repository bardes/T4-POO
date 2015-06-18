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
`{CONTEÚDO}`: Pode ser qualquer sequencia de caractere ASCII válidos, exeto caracteres de controle. *Note que espaços após os ':' são interpretados como parte da variável!*.
`<\n>`: Representa uma quebra de linha.
A parte entre colchetes pode ser repetida 0 ou mais vezes.

A respostas do servidor segue o mesmo modelo da requisição, porém além do cabeçalho (delimitado pelas duas quebras de linhas seguidas) a resposta pode conter mais informações à variar de acordo com o comando que gerou a resposta.

### Exemplos:
Cliente envia:
```
LOGIN
user:nome_de_exemplo
pass:s3nh4_s3cr374

```
Resposta:
```
OK
token:uPezJ4+KOqDuTjydvmRzhw==

```
Ou:
```
ERRO

Usuário e/ou senha inválidos.
```
### Comandos válidos:
#### LOGIN
```
LOGIN
user:nome_de_exemplo
pass:s3nh4_s3cr374

```

