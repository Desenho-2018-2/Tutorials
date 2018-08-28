# DOCKER LAB 101
### Instalar: Virtual Box

https://www.virtualbox.org

### Instalar: Vagrant

https://www.vagrantup.com

### Preparando o laboratório

`unzip docker-lab.zip`

`cd docker-lab/box`

`vagrant box add <box-image.box>`

`cd ..`

`vagrant up`

### Instalar: Docker

`sudo dnf install -y docker`

###### Verificar o status do serviço Docker

`sudo systemctl status docker`

###### Iniciando o serviço Docker

`sudo systemctl start docker`

###### Habilitando o serviço Docker na inicialização

`sudo systemctl enable docker`

###### Verificando a instalação

`sudo docker info`

###### Criando grupo docker

`sudo groupadd docker`

###### Adicionando usuário ao grupo

`sudo usermod -aG docker vagrant`

###### Verificando os grupos do usuário

`id vagrant`

###### Reiniciando o serviço Docker

`sudo systemctl restart docker`

###### Realizar novo login na máquina Vagrant

`exit`

`vagrant ssh`

###### Verificando a permissão

`docker info`

###### Verificando a instalação do docker

`docker container run hello-world`

### Começando com o Docker

`docker container run -it ubuntu /bin/bash`

A flag **-i** mantêm a STDIN do contêiner aberta;

A flag **-t** cria uma pseudo-tty para o contêiner;

Primero o Docker verifica qual imagem queremos utilizar para criar nosso contêiner, neste caso **ubuntu**, mas poderia ser qualquer outra (fedora, debian, centos). Essas imagens são consideradas "base" e são fornecidas pela Docker Inc através do Docker Hub.

Uma verificação local é feita, caso não encontre a imagem ele busca no registry Docker Hub e baixa a imagem para a máquina local. O contêiner tem rede, um endereço IP e uma interface em modo bridge para se comunicar com o host.

###### Vamos explorar nosso primeiro contêiner

`hostname`

`cat /etc/hosts`

`ip a`

`ps -aux`

`top`

`exit`


### Verificando o status do nosso contêiner

O que ocorreu com nosso contêiner? Ele parou de executar, pois ele só continua executando enquanto o nosso comando estiver rodando.

Para ver a lista completa de contêiners digite:

`docker container ps -a`

A flag **-a** exibe todos os contêiners, tanto os que estão parados ou executando.

A flag **-l** exibe o último contêiner executado, independente se ele está parado ou executando.


Muitas informações aqui:

Container Id - Identificador do contêiner

Image - Imagem usada para criar o contêiner

Command - Último comando que executou

Created - Quando foi criado

Status - Código de retorno do processo (exit code)

Names - Nome do contêiner (neste caso gerado dinâmicamente)

### Identificando nosso contêiner

Podemos dar nomes aos nossos contêiners através da flag **--name**, por exemplo:

`docker container run --name web -it ubuntu /bin/bash`

`exit`

Nomes são únicos, não podem existir dois contêiners com o mesmo nome. É preciso apagar o contêiner antigo com o mesmo nome. Vamos verificar o contêiner recém criado:

`docker container ps -a`

### Iniciando nosso contêiner

Nosso contêiner está parado, podemos inicia-lo novamente usando:

`docker container start bob_the_container`

Ou usando o seu ID:

`docker container start aa3f365f0f4e`

Se rodarmos nosso comando **ps** sem a flag **-a** iremos perceber que nosso contêiner está sendo executado.


### Conectando ao nosso contêiner

Nosso contêiner vai iniciar com as mesmas opções que nós executamos com o **docker container run**. Desta forma existe uma shell nos aguardando no contêiner. Para conectar novamente usamos:

`docker container attach bob_the_container`

Saindo da shell, novamente nosso contêiner irá parar.

### Criando contêiner com daemons

Além de contêiners interativos, podemos criar contêiners que rodam serviços ou aplicações em background.

`docker container run --name daemon_dave -d ubuntu /bin/sh -c "while true; do echo hello world; sleep 1; done"`

A flag **-d** indica ao Docker para rodar o serviço em background (detach).

Podemos ver nosso contêiner executando:

`docker container ps`


### Vendo o que está acontecendo em nossos contêiners

#### Logs

Para visualizar o que está ocorrendo dentro dos nossos contêiners podemos utilizar o seguinte comando:

`docker container logs daemon_dave`

O Docker vai exibir as últimas saídas e retornar ao prompt. Para visualizar de forma contínua devemos utilizar a flag **-f** da mesma forma que é utilizada no comando tail.

`docker container logs -f daemon_dave`

Use CTRL + C para sair.

#### Processos

Para visualizar os processos que estão rodando em nosso contêiner usamos:

`docker container top daemon_dave`

#### Estatísticas

Em relação ao uso de recursos (CPU, memória, rede, IO), existe o comando **stats** que exibe muitas informações relevantes:

`docker container stats daemon_dave <conteiner 1> <conteiner 2>`

### Rodando um processo dentro do contêiner

Através do comando **exec** podemos executar comandos dentro do nosso contêiner:

* Background

`docker container exec -d daemon_dave touch /etc/new_config_file`

* Interativo

`docker container exec -it daemon_dave /bin/bash`

### Parando um contêiner

Para parar um contêiner usamos o comando **stop** da seguinte forma:

`docker container stop daemon_dave`

O Docker envia um sinal SIGTERM para o processo que está executando atualmente. Pode ser utilizado o comando **kill** se for necessário uma parada mais brusca (SIGKILL).

### Restart automático do contêiner

O Docker pode monitorar e reiniciar caso necessário o processo iniciado através do comando **run**:

`docker container run --restart=always --name daemon_dave -d ubuntu /bin/sh -c "while true; do echo hello world; sleep 1; done"`

Neste caso o Docker vai tentar reiniciar o contêiner independente do exit code retornado pelo processo.

Para definir um limite pode ser utilizado:

`--restart=on-failure:5`

Neste caso somente em caso de falha (exit code diferente de zero) é que o Docker vai tentar reiniciar o contêiner.

### Sabendo mais sobre o contêiner

Para ter maiores informações sobre nosso contêiner, podemos pedir informações de metadados ao Docker:

`docker container inspect daemon_dave`

Para obter apenas alguma informação específica podemos utilizar a flag **--format**:

`docker container inspect --format '{{ .NetworkSettings.IPAddress }}' daemon_dave`

`docker container inspect --format '{{.Name}} {{.State.Running}}' daemon_dave <conteiner 1>`

### Apagando um contêiner

Quando não precisar mais de um contêiner, você pode apaga-lo com o comando **container rm**:

`docker container rm <nome ou ID>`

Para apagar todos os contêiners:

``docker container rm `docker container ps -a -q` ``

**De volta aos slides para entender o que é uma imagem Docker.**

### Listando imagens Docker

Para obter uma lista de imagens usamos o comando **image ls**:

`docker image ls`

As imagens e outros arquivos internos do Docker estão no diretório **/var/lib/docker**.

``sudo ls -l /var/lib/docker``

As imagens vivem em **repositórios** e os **repositórios** vivem nos **registries**.

O registry padrão é o Docker Hub, que geralmente tem código open-source. Se precisar, você pode criar seu próprio registry corporativo, com acesso restrito.

### Baixando imagens dos repositórios

Para baixar uma imagem específica utilizamos o comando **image pull**:

`docker image pull ubuntu:12.04`

O que vêm depois dos dois pontos é uma **tag**. Uma imagem pode ter mais de uma tag caso necessário. Isso permite que mais de uma imagem seja armazenada em um repositório.

Vejamos no Docker Hub quais as tags disponíveis temos para o Ubuntu.

Existem 2 tipos de repositórios:

* repositório de usuários
* repositórios top-level

No repositório de usuário, temos o formato:

odilontalk/puppet

Onde o nome de usuário neste caso é **odilontalk** e o nome do repositório é **puppet**.

No caso dos repositórios top-level, como **ubuntu** ou **fedora**, estes repositórios são considerados oficiais e são escolhidos a dedo pela Docker Inc. pois são imagens bem construídas, seguras e atualizadas.


Quando nenhuma tag é informada, implicitamente é usada a tag **latest**.


### Procurando imagens

Para descobrir novas imagens que podem ser úteis na hora de montar nosso ambiente ou stack de tecnologia, utilizamos o comando **search**:

`docker search puppet`

Por exemplo, para baixar uma imagem feita por um usuário que contêm o Puppet Master Server:

`docker image pull jamtur01/puppetmaster`

`docker container run -it jamtur01/puppetmaster /bin/bash`

`puppet --version`


### Construindo imagens

Como podemos criar nossas próprias imagens?

Até o momento nós pegamos imagens prontas, mas podemos criar a nossa modificando uma imagem existente e enviando para o registry de onde poderão ser baixadas e executadas nos mais diversos ambientes.

#### Criando uma conta no Docker Hub

Visitar:

https://hub.docker.com/

E criar uma nova conta.

Vamos usar o comando **login** para autenticar no Docker Hub:

`docker login`

Insira o usuário e a senha criados no passo anterior.


### O arquivo Dockerfile

A maneira mais direta de se criar uma imagem Docker personalizada é utilizando um arquivo chamado `Dockerfile` que é como uma receita de bolo de como montar nossa imagem.

Cada passo executado neste arquivo cria uma imagem intermediária com as alterações que foram feitas no passo anterior.

Vamos criar um imagem personalizada que contêm um servidor web para exemplificar:

`mkdir docker-projeto-web`

`cd docker-projeto-web`

`touch Dockerfile`

Agora vamos alterar o arquivo para ter este conteúdo:

`nano Dockerfile`

```
FROM ubuntu:14.04
MAINTAINER Seu Nome "seu@email.com"
RUN apt-get update && apt-get install -y nginx
RUN echo 'Hello from Docker conteiner!' >/usr/share/nginx/html/index.html
EXPOSE 80
```

Para construir a nova imagem usamos o comando **image build** do diretório onde o arquivo Dockerfile está localizado:

`docker image build -t="seu_usuario/projeto_web" .`

Usamos a flag **-t** para marcar nossa imagem criado com um repositório e um nome. Neste caso **seu_usuario** é o repositório e **projeto_web** é o nome da imagem. Você tambêm pode colocar tags na imagem:

`docker image build -t="seu_usuario/projeto_web:v1" .`

Lembrando que se nenhuma tag for adicionada, a tag **latest** é adicionada implicitamente.

Neste caso foi utilizado o "." para representar o diretório atual, mas poderia ser passado um repositório do Github:

`docker image build -t="jamtur01/static_web:v1" git@github.com:seu_usuario/projeto_web`

Neste cenário, o Docker assume que o Dockerfile está na raiz do repositório.

É feito o upload dos arquivos da pasta atual para o Docker daemon e lá a imagem é construída.

#### O arquivo .dockerignore

É possível criar um arquivo nos mesmos moldes que o `.gitignore` para não fazer upload de arquivos desnecessários para montar a imagem.

Lembrando que quanto maior o tamanho dos arquivos presentes no diretório de build, mais tempo irá levar para construir sua imagem, além de criar uma imagem maior sem necessidade. Por isso não suba arquivos desnecessários para sua imagem.  

#### Visualizando sua imagem

Para ver nossa imagem recém criada:

`docker images seu_usuario/projeto_web`

Para entender melhor como ela foi criada podemos utilizar o comando **image history**:

`docker image history seu_usuario/projeto_web`

#### Rodando um contêiner com a nossa nova imagem

Agora que temos uma nova imagem criada podemos criar um novo contêiner:

`docker container run -d -p 80 --name meu_site seu_usuario/projeto_web nginx -g "daemon off;"`

O significado das flags:

**-d** : como foi visto anteriormente, roda em backgroud (detach).

**-p 80** : diz ao Docker que a porta 80 deve ser publicada em runtime.

Existe duas estatégias para o Docker atribuir a porta:

* Uma porta randômica entre o range 32768 até 61000 que vai ser mapeada para a porta 80 do contêiner.
* Você especifica a porta que será mapeada para a 80 do contêiner no formato `-p <porta externa>:<porta no container>`.

Use o comando `docker container ps -l` para verificar qual a porta foi escolhida no comando `run`, veja a coluna **PORTS**.

Ou utilize o comando:

`docker container port 0c699ccc4307 80`

Para escolher a porta podemos utilizar:

`docker container run -d -p 8080:80 --name meu_site seu_usuario/projeto_web nginx -g "daemon off;"`

Ou uma interface específica:

`docker container run -d -p 127.0.0.1:80:80 --name meu_site seu_usuario/projeto_web nginx -g "daemon off;"`

Ou todas as portas que foram declaradas no Dockerfile via **EXPOSE**:

`docker container run -d -P --name meu_site seu_usuario/projeto_web nginx -g "daemon off;"`


Para verificar que o nosso contêiner está de fato rodando o servidor web:

`curl localhost:49154`


Para uma referência completa dos comandos existentes no arquivo Dockerfile, veja o link:

https://docs.docker.com/engine/reference/builder/


## Mais de um contêiner?

E quando precisamos de mais de um contâiner?

Por exemplo um servidor Web e um Redis para gravar dados? Como orquestrar todos esses componentes? 

Para isso iremos aprender a usar o **Docker Compose**!

###### Instalando o Docker Compose

`sudo curl -L https://github.com/docker/compose/releases/download/1.22.0/docker-compose-$(uname -s)-$(uname -m) -o /usr/local/bin/docker-compose`

`sudo chmod +x /usr/local/bin/docker-compose`

`docker-compose --version`

###### Criando nosso projeto

Vamos criar um aplicação web na linguagem Python que conecta ao Redis para gravar dados para mostrar o uso do docker compose.

Criando o diretório do projeto:

`mkdir composer`

`cd composer`

##### Componente Web

Criar o arquivo **app.py** com o seguinte conteúdo:

`nano app.py`

```
import time

import redis
from flask import Flask


app = Flask(__name__)
cache = redis.Redis(host='redis', port=6379)


def get_hit_count():
    retries = 5
    while True:
        try:
            return cache.incr('hits')
        except redis.exceptions.ConnectionError as exc:
            if retries == 0:
                raise exc
            retries -= 1
            time.sleep(0.5)


@app.route('/')
def hello():
    count = get_hit_count()
    return 'Hello World! I have been seen {} times.\n'.format(count)

if __name__ == "__main__":
    app.run(host="0.0.0.0", debug=True)
```

Criar o arquivo **requirements.txt** com o seguinte conteúdo:

`nano requirements.txt`

```
flask
redis
```

Criar o arquivo **Dockerfile** com o seguinte conteúdo:

`nano Dockerfile`

```
FROM python:3.4-alpine
ADD . /code
WORKDIR /code
RUN pip install -r requirements.txt
CMD ["python", "app.py"]
```

##### Composição: juntando mais de um serviço

Criar o arquivo **docker-compose.yml** com o seguinte conteúdo:

`nano docker-compose.yml`

```
version: '3'
services:
  web:
    build: .
    ports:
     - "5000:5000"
  redis:
    image: "redis:alpine"
```    

Perceba que neste arquivo temos mais de um serviço, no caso **web** e **redis**. No caso do serviço web a imagem docker é construída a partir do diretório atual. O serviço redis por sua vez utiliza uma imagem pronta e disponível no Docker Hub.

Vamos testar essa composição, executando:

`docker-compose up`

Visite a aplicação web no seu navegador usando:

`http://192.168.33.10:5000`


Abra outro terminal, acesse a máquina Vagrant e execute:

`cd <diretorio do lab>`

`vagrant ssh`

`docker image ls`


Iremos alterar o arquivo **docker-compose.yml** para adicionar um ponto de montagem:

```
version: '3'
services:
  web:
    build: .
    ports:
     - "5000:5000"
    volumes:
     - .:/code
  redis:
    image: "redis:alpine"
```    

Execute novamente o projeto:

`docker-compose up`

Altere o arquivo **app.py** próximo da linha 26 com o seguinte conteúdo:

```
return 'Hello from Docker! I have been seen {} times.\n'.format(count)
```

Veja a alteração ser feita a quente, sem precisar construir a imagem docker novamente como era feito no processo anterior.

Existem diversos outros comandos úteis no docker-compose por exemplo:

`docker-compose -d` - executa em background

`docker-compose run <servico> <comando>` - executa um comando no serviço especificado

`docker-compose stop` - para todos os serviços




