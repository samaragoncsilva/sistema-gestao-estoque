# Sistema de Gestão de Estoque de Distribuidora

Projeto acadêmico desenvolvido em **Java** para a disciplina de **Programação Orientada a Objetos**. A aplicação funciona pelo terminal e permite administrar usuários, clientes, fornecedores e produtos de uma distribuidora.

## Funcionalidades

- **Funcionários:** cadastro, consulta, edição, listagem por nível de acesso e desativação de contas.
- **Clientes e fornecedores:** cadastro, consulta por documento, edição e desativação, preservando os registros.
- **Produtos:** cadastro, consulta, edição e exclusão; entradas e saídas de estoque, com impedimento de saídas superiores à quantidade disponível.
- **Estoque:** identificação de produtos com estoque baixo e relatório de produtos ordenado por quantidade.
- **Autenticação e permissões:** acesso por login e senha, com autorização conforme os níveis `ADM_PADRAO`, `ADM_AVANCADO`, `ADM_TOTAL`, `GERENTE`, `ESTOQUISTA` e `VENDEDOR`.
- **Persistência:** armazenamento local dos dados para recuperação em execuções posteriores.

## Requisitos

- **Java 17** ou superior (JDK).
- Terminal para compilar e executar o programa.

Não são necessárias bibliotecas externas.

## Compilação e execução

Abra o terminal **na pasta raiz do repositório** e execute:

```bash
javac src/*.java
java -cp src Main
```

Na primeira execução, sem usuários cadastrados, o programa solicita a criação de um administrador inicial com nível `ADM_TOTAL`. Depois disso, o acesso é feito pela opção **Fazer login**.

## Armazenamento dos dados

O sistema utiliza serialização de objetos Java e cria automaticamente a pasta `dados/` quando realiza a primeira gravação. Os registros são armazenados separadamente em:

- `dados/usuarios.dat` — funcionários, clientes e fornecedores.
- `dados/produtos.dat` — produtos.

Esses arquivos não são versionados pelo Git. Para uma apresentação com registros previamente cadastrados, mantenha a pasta `dados/` no computador em que o programa será executado.

## Organização do código

Os arquivos Java estão na pasta `src/`. A classe `Main` inicia o programa e integra os menus; `Persistencia` realiza a gravação e a leitura dos arquivos; `Usuario`, `Funcionario`, `Cliente`, `Fornecedor` e `Produto` representam as entidades; `NivelAcesso` define os níveis de acesso.