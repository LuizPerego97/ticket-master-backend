Ticket Master Backend
Uma API REST de alta performance desenvolvida para simular o ecossistema crítico de venda de ingressos e reserva de assentos em tempo real. O grande diferencial deste projeto não é apenas o CRUD, mas sim a blindagem da aplicação contra cenários severos de concorrência e vendas duplas.

🚀 O Desafio Técnico
Em sistemas de bilheteria, o maior pesadelo dos engenheiros de software é a venda dupla (dois usuários tentando comprar o mesmo assento no exato mesmo milissegundo). Se a aplicação crescer e passar a rodar atrás de um Load Balancer com múltiplos servidores em paralelo, o controle de memória tradicional do Java (synchronized, etc.) deixa de funcionar.

Este projeto foi desenhado especificamente para resolver esse problema de consistência distribuída, delegando a verdade absoluta dos dados ao banco de dados e mantendo a aplicação totalmente stateless.

🛠️ Tecnologias Utilizadas
Java 21 / 25 (Utilizando recursos modernos da linguagem)

Spring Boot 3.x (Core da aplicação, gerenciamento de injeção de dependências e web REST)

Spring Data JPA & Hibernate (Camada de persistência e mapeamento objeto-relacional)

Oracle Database (Banco de dados relacional robusto com forte consistência ACID)

Lombok (Produtividade e eliminação de código boilerplate)

Jakarta Validation (Proteção e validação de payloads na camada de entrada)

🧠 Arquitetura e Engenharia de Software
1. Proteção de Transações Finitas (Lock Pessimista)
Para garantir o princípio de Isolamento do ACID, a rota de reserva de assentos utiliza Pessimistic Locking (PESSIMISTIC_WRITE).
Quando a requisição chega:

O Spring instrui o Oracle a executar um SELECT ... FOR UPDATE na linha do assento.

O Oracle tranca a linha para aquela transação específica.

Se outra instância da API tentar ler o mesmo assento naquele instante, ela entra em fila ou é barrada pela exception PessimisticLockingFailureException.

O status é atualizado para ocupado, a transação faz o COMMIT e o cadeado é liberado de forma segura.

2. Camada de Transição Limpa (DTO Único por Entidade)
Para evitar o vazamento de entidades gerenciadas pelo Hibernate para a camada de controle (@RestController) e blindar o contrato da API, foi adotado o padrão de DTO Único:

O cliente envia apenas IDs primitivos no payload de entrada (customerId, seatId).

O service processa a regra de negócio e enriquece o DTO de saída, devolvendo os relacionamentos aninhados prontos para o consumo do front-end, evitando requisições extras à API.

3. Validação de Dados na Entrada
Uso rigoroso de anotações do Jakarta Validation (@NotNull, @NotBlank) para garantir a integridade dos dados antes mesmo deles tocarem a camada de serviço ou gastarem conexões com o banco de dados.

🗄️ Estrutura do Banco de Dados (Oracle)
A aplicação utiliza chaves primárias numéricas gerenciadas via Sequences dedicadas no Oracle, otimizando a performance de escrita e evitando conflitos de geração de ID comuns em estratégias baseadas em tabelas de identidade padrão.

As entidades principais do ecossistema são:

Customer (Cliente): Cadastro de usuários compradores.

Seat (Assento): Gerenciamento físico de fileiras, números e estados de ocupação.

Transaction (Transação): A tabela pivot que amarra o cliente ao assento consumido após o sucesso do lock.

🔧 Como Rodar o Projeto Localmente
Pré-requisitos
JDK 21 ou superior instalado.

Instância do Oracle Database ativa.

IDE de sua preferência (IntelliJ IDEA recomendada).
