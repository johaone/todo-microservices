В этом пакете находится один исз способов отправки сообщений в "брокер сообщений" - RabbitMQ
Этот подход подразумевает применение интерфейсов Source(отправитель), Source(получатель), и Processor(и то и другое).
В этих интерфейсах описываются методы отправки и получения по каналам отправки и приема, которые связываются между собой
в определенном exchange (указывается destination в файлах properties и принимающего и отправляющего сообщения микросервисов),
и аннотируются через специальные аннотации @InputBinding и @OutputBinding. Эти интерфейсы имплементируют классы,
выступающего в качестве Producer и Consumer соответственно. Но это подход устарел, а взамен применяется
функциональный код.