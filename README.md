<h3>GRPC</h3>
GRPC - framework que utiliza a tecnologia RPC para fazer a comunicação.


RPC é como se estivesse chamando uma função localmente. Só que essa função vai até um servidor. Ela trafega pela rede e vai até um servidor.
Por exemplo, o cliente está fazendo uma requisição "soma" passando o parâmetro 1 e 2. No código, o cliente vai executar isso como se fosse algo local mesmo. Há uma função local, onde o GRPC e o protobuf vão trabalhar por baixo dos panos para levar essa requisição até o server, e o server vai devolver a resposta.
Se no RPC tem um cliente que faz uma chamada para um servidor e essa chamada pode trafegar pela rede, o GRPC serve para fazer essa ponte, pois carrega dados estruturados (serializados pelo protobuf),então o GRPC pega uma classe (HelloWord.java) e a entrega no servidor (HelloWorld.class), indicando o caminho inverso (no caso de requisições com requeste e response). No caso, o protobuf é o responsável por fazer a revelação dos dados.


O GRPC faz a comunicação, só que ele precisa desses dados serializados. Em uma classe, de um lado é preciso serializá-la e transmiti-la pela rede e do outro lado, é preciso deserializá-la (obter novamente os dados legiveis para serem usados). O protobuf faz a serialização e deserialização, ele utiliza o arquivo proto de linguagem neutra, onde nele é definido mensagens (seria como parâmetros de um objeto) e serviços (seria como interface, assinatura de um método). O protobuf lê o arquivo proto e o transforma em uma classe (HelloWorld.class). 

RPC Unário - é o mais simples deles, basicamente um cliente envia uma requisição e o servidor devolve uma resposta (muito parecido com talvez um GET e POST).

RPC stream de servidor - o cliente envia uma requisição e o servidor ao invés de devolver somente uma requisição, devolve um fluxo "stream" de respostas.

RPC stream de cliente - têm-se a figura de um cliente que envia um fluxo de request para o servidor, assim ele pode processar esse fluxo ou esperar todos chegarem para processar ou processar cada uma dessas flechas de cada vez. Isso vai da implementação do código de acordo com a necessidade das regras de negócio. Então, ele vai receber um fluxo de requisições do cliente e ele devolve somente com a resposta.

RPC stream bidirecional - há uma união do stream cliente com um stream servidor. Basicamente, tem-se um fluxo de requisições recebidas, essas requisições são processadas cada um ou um grupo de requisições, de acordo com o que foi estabelecido dentro do servidor e ele vai devolver as respostas. Basicamente, abre-se um fluxo e recebe um fluxo de volta.


Casos de uso:

- ideal para microserviços;
- mobile, browsers backend;
- geração das bibliotecas de forma automática;
- streaming bidirecional utilizando HTTP/2 (cliente pode mandar pacote "steaming de dados" para o servidor,da mesma forma que o servidor pode fazer isso numa única conexão);


Linguagens (suporte oficial):

- gRPC-GO
- gRPC-JAVA
- gRPC-c

RPC - Remote Procedure Call:
Servidor tem uma função, assim o cliente chama um servidor (cria um processo no client dele, onde consegue estabelecer um conexão com o servidor) e executa essa função no server. Assim, faz-se uma request que onde pode-se trabalhar. 

Protocol Buffer:

- É uma linguagem e plataforma neutra, onde tem processos de extensão (independente da plataforma, consegue enviar dados de um lado para outro, envio e recebimento de dados). 

![alt text](https://github.com/Jennyads/Database_architecture_in_graphs_and_GRPC/blob/master/Imagens/Captura%20de%20tela%20de%202023-03-03%2018-28-08.png)

Protocol Buffer vs JSON:

- Arquivos binários (arquivo reduzido para falar na linguagem de máquina) > JSON (arquivo no formato humano, consegue olhar e ler).
- Processo de serialização é mais leve (CPU) do que JSON;
- Gasta menos recursos de rede;


Para enviar esses dados via HTTP teria que gastar espaço com caracteres desnecessários, como colchetes e chaves.

````
const tarefa = {
  id:1,
  descricao: 'Lavar a louça'
  data: '12/03/2022'
  responsavel: 'Gabriel',
  feita: false
  
}
const listaDeTarefas = [tarefas]
````

````
syntax "proto3"

message Tarefa {
  int32 id = 1;
  string descricao = 2;
  string data = 3;
  string responsavel = 4;
  bool feito = 5;
}

message ListaDeTarefas {
  repeated Tarefa lista = 1;
 }
 ````
 
No protobuf por outro lado, chama-se cada tipo de dado de mensagem, e essa mesma lista seria definida rápidamente. Os números do lado direito são índices da mensagem, eles existem para dizer qual campo é o que
quando a mensagem for enviada, assim isso elimina a necessidade de codificar caracteres desnecessário, considerando-se que pode passar só um número para dizer qual é o próximo campo e o tipo desse campo. 

Se há dois sistemas,  sendo um cliente e um servidor, os dois lados vão precisar ter a mesma versão de um arquivo protobuf que termina com a extensão proto, quando se lê esse arquivo e se passa a configuração para o GRPC, ele automaticamente sabe qual o tipo de ação foi chamada e qual é a mensagem que está sendo recebida.
O protobuf não define só a mensagem, mas sim toda API, dessa forma, vai ter toda a comunicação do sistema definida nesse arquivo e vai ser com base nela que o GRPC vai trabalhar. 

Primeiro é necessário definir os outros tipos que vão existir na nossa API, além dos dois que a gente já criou, o tipo vazio que significa que o valor será em branco e o tipo tarefaID de que vai ser somente o ID de uma tarefa. Depois é necessário definir em tudo que o API (ou como o protobuf chama, o serviço vai ter).
````

syntax "proto3";

message Vazio {}

message TarefaID {
    int32 id =1;
}
````

Nessa API,pode ter três ações listar todas as tarefas, adicionar uma tarefa e marca a tarefa como concluída. Para isso, será necessário criar a representação do serviço que vai conter várias chamadas RPC. Cada chamada RPC só pode conter uma mensagem de entrada e uma mensagem de saída, por isso que teve que definir tipos diferentes para tarefa e o ID da tarefa, se a
juntar tudo que se tem, têm-se o arquivo de definição completo da API. 

```
syntax "proto3";

service APIListaDeTarefas {
  rpc ListarTodas (Vazio) returns (ListadeTarefas);
  rpc AdicionarTarefa (Tarefa) returns (Tarefa);
  rpc MarcarComoConcluida (TarefaID) returns (Tarefa);
}
````

Quando se inicia um novo serviço de GRPC, trata-se da comunicação entre duas partes (servidor e cliente).Durante a inicialização, é necessário passar o caminho desse arquivo protobuf para o GRPC que por sua vez vai ler o arquivo, fazer um "parsing", ou seja, decodificar o arquivo para poder entender de dentro e trazer a definição da API como um objeto que pode ser lido pelo motor de execução dele. 
```
//javascript, grpc tem uma biblioteca proprio
const grpc = require('grpc')
const path = require('path')
const DefinicaoTarefas = grpc.load(path.resolve('./tarefas.proto'))
```

o GRPC leva como parâmetro um objeto que vai ter uma lista das implementações reais de cada ação que foi definida no arquivo protobuf. Então é necessário ter que ter três funções, uma para listar, marcar como concluído, adicionar tarefa.

```
const grpc = require('grpc')
const path = require('path')
const DefinicaoTarefas = grpc.load(path.resolve('./tarefas.proto'))

const server = new grpc.Server()
server.addService(
  DefinicaoTarefas.APIListaDeTarefas.service,
  { /* implementação */ }
}
```
Listar todas as tarefas: 
Cria-se uma lista de tarefas que já vai ter uma tarefa dentro, assim cria-se uma função com o mesmo nome
do RPC no arquivo protobuf, a função de GRPC leva dois parâmetros, sendo o primeiro requisição feita pelo cliente, por meio dela vai buscar todos os parâmetros que foram passados para o servidor, nesse caso o parâmetro é vazio. O segundo parâmetro é um callback que vai responder para o cliente quando for chamado, o callback também uma função, e o primeiro parâmetro dela é a mensagem de erro se passar alguma coisa que não seja nula, o GRPC vai retornar um erro para o cliente. já o segundo parâmetro é odado que vamos retornar que precisa ser do mesmo tipo que foi definido lá no arquivo protobuf. Depois é só adicionar 
adicionar essa função no objeto de implementação GRPC, fazendo o mesmo para as outras duas funções.

```
const tarefas = [
  {
    id:1,
    descricao: 'Lavar a louça'
    data: '12/03/2022'
    responsavel: 'Gabriel',
    feita: false
   }
]
function ListarTodas(requisicao, callback) {
  return callback(null, tarefas)
}
````
Adicionando a  função no objeto de implentação no GRPC, fazendo o mesmo para as duas outras. Assim, fica tudo junto em um objeto só de implementação!

```
const grpc = require('grpc')
const path = require('path')
const DefinicaoTarefas = grpc.load(path.resolve('./tarefas.proto'))

const tarefas = [
  {
    id:1,
    descricao: 'Lavar a louça'
    data: '12/03/2022'
    responsavel: 'Gabriel',
    feita: false
   }
]

function ListarTodas(requisicao, callback) {
  return callback(null, tarefas)
}

function AdicionarTarefa(requisicao, callback) {
  const {id, data, descricao, responsavel} = requisicao.request
  tarefas.push({
    id,
    data,
    descricao,
    responsavel,
    feito: false
   })
   return callback(null, tarefas.at(-1))

}

function MarcarComoConcluida (requisicao, callback) {
  const {indice, tarefaConcluida} = tarefas.find((tarefa, indice) => {
    if(tarefa.id === requisicao.request.id) {
      return {
        indice,
        tarefa
       }
      }
    })
    
    if (!tarefaConcluida) {
      return callback(new Error('A tarefa não existe'), null)
     }
     
     tarefaConcluida.feito = true
     tarefas[indice] = tarefaConcluida
     return callback(null, tarefaConcluida)
    }
    
const server = new grpc.Server()
server.addService(
  DefinicaoTarefas.APIListaDeTarefas.service,
  { ListarTodas, AdicionarTarefa, MarcarComoConcluida}
)   
````

Finalizando o lado do servidor:
Escolheu-se uma porta para poder ouvir a comunicação, com HTTP/2 é brigado a ter um certificado digital, para testes locais ele dá um certificado de "mentira" como: grpc.ServerCredentials.createInsecure.

````
const grpc = require('grpc')
const path = require('path')
const DefinicaoTarefas = grpc.load(path.resolve('./tarefas.proto'))

const tarefas = [...
]

function ListarTodas(requisicao, callback) {...
}

function AdicionarTarefa(requisicao, callback) {...
}

function MarcarComoConcluida (requisicao, callback) {...
}
    
const server = new grpc.Server()
server.addService(
  DefinicaoTarefas.APIListaDeTarefas.service,
  { ListarTodas, AdicionarTarefa, MarcarComoConcluida}
) 

server.bind('0.0.0.0:50051', grpc.ServerCredentials.createInsecure())
server.start()

````
Vatangens GRPC: O cliente pode se comunicar com o servidor sem saber nenhum detalhe de rede do ambiente ou do próprio servidor. 
O GRPC abstrai todas as chamadas de rede, a chamada de um cliente para um servidor é exatamente igual a uma chamada de uma função local, como se tivesse um objeto dentro do próprio código.

Passando o endereço do servidor e das credenciais, pode-se chamar cada uma das funções do servidor, como se fossem métodos do cliente.

```
const grpc = require('grpc')
const path = require('path')
const DefinicaoTarefas = grpc.load(path.resolve('./tarefas.proto'))

const client = new DefinicaoTarefas.APIListaDeTarefas(
  'localhost:50051',
  grpc.credentials.creatInsecure()
)
client.listarTodas({}, (err, tarefas) => {
  if(err) trrow err
  console.log(tarefas)
})

const novaTarefa = {
  id: 2,
  descricao: 'Passear com o cachorro',
  data: '02/03/2022',
  responsavel: 'Vanessa',
  feito: false
}

client.adicionarTarefa(novaTarefa, (err, tarefa) => {
  if(err) throw err
  console.log(tarefa)
}

client.marcarComoConcluido({id:2}), (err, tarefa) => { 
  if(err) throw err
  console.log(tarefa)
})
    
````
