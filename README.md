# The Great Programming Journey

**The Great Programming Journey** é um jogo de tabuleiro digital desenvolvido em **Java**, onde múltiplos "programadores" competem numa corrida até à glória final (a última casa do tabuleiro)!

O projeto aplica conceitos fundamentais de **Programação Orientada a Objetos (POO)**, incluindo herança, polimorfismo, classes abstratas, enumerações, coleções (`ArrayList`, `HashMap`, `HashSet`) e gestão de estado.

---

## 🚀 Funcionalidades do Jogo

* **Multijogador:** Suporte para 2 a 4 jogadores.
* **Personalização:** Cada jogador possui um Nome, ID, Cor (`Blue`, `Green`, `Purple`, `Brown`) e uma Linguagem de Programação favorita.
* **Mecânicas de Movimento:**
    * Lançamento de dados (1 a 6 casas).
    * **Restrições de Linguagem:** Linguagens de "baixo nível" são mais difíceis de programar!
        * **Assembly:** Avança no máximo 2 casas por turno.
        * **C:** Avança no máximo 3 casas por turno.
    * **Ricochete:** Se o movimento ultrapassar a última casa, o jogador recua o número de casas em excesso.
* **Sistema de Eventos:** O tabuleiro contém surpresas:
    * **Abismos:** Obstáculos que prejudicam o jogador (recuar, perder turnos ou perder o jogo).
    * **Ferramentas:** Itens úteis que o jogador recolhe para se proteger automaticamente contra abismos específicos.
* **Persistência:** Capacidade de Salvar e Carregar o estado do jogo através de ficheiros de texto.

---

## 🛠️ Elementos do Jogo

### Ferramentas (Bónus)
As ferramentas são acumuladas pelo jogador e consumidas para anular o efeito de certos abismos.

| Ferramenta | Protege Contra |
| :--- | :--- |
| **Herança** | *Code Duplication* |
| **Programação Funcional** | *Infinite Loop*, *Secondary Effects* |
| **Testes Unitários** | *Logic Error* |
| **Tratamento de Excepções** | *Exception*, *File Not Found* |
| **IDE** | *Syntax Error* |
| **Ajuda do Professor** | *(Item colecionável genérico)* |

### Abismos (Obstáculos)
Representam os pesadelos de qualquer programador durante o desenvolvimento:

* **Syntax Error:** Recua 1 casa (Anulado por *IDE*).
* **Logic Error:** Recua metade do valor do dado (Anulado por *Testes Unitários*).
* **Exception:** Recua 2 casas (Anulado por *Tratamento de Excepções*).
* **File Not Found:** Recua 3 casas (Anulado por *Tratamento de Excepções*).
* **Crash:** O programa crashou! Recua para a casa inicial (Start).
* **Code Duplication:** Recua o valor total do dado lançado (Anulado por *Herança*).
* **Secondary Effects:** Recua para a posição onde estava há 2 turnos atrás (Anulado por *Prog. Funcional*).
* **Infinite Loop:** O jogador fica **PRESO** no tabuleiro. Só sai se tiver *Prog. Funcional* ou se outro jogador passar na mesma casa e o "libertar".
* **Segmentation Fault:** Se houver 2 ou mais jogadores na casa, todos recuam 3 casas.
* **Blue Screen of Death:** O erro fatal. O jogador é **DERROTADO** e eliminado do jogo.

---

## 🏗️ Estrutura do Projeto

O código encontra-se organizado no pacote `pt.ulusofona.lp2.greatprogrammingjourney`.

| Componente | Descrição |
| :--- | :--- |
| **GameManager** | Controlador principal ("God Class"). Gere o fluxo do jogo, validação de jogadores, turnos e I/O (load/save). |
| **Board** | Representa o tabuleiro, contendo a lista de `Slot`s e gerindo a lógica de movimentação e posicionamento. |
| **Slot** | Cada casa do tabuleiro. Pode conter uma lista de `Player`s e um `Event` (`Abyss` ou `Tool`). |
| **Player** | Representa o jogador, guardando o seu estado (`EM_JOGO`, `PRESO`, `DERROTADO`), inventário de ferramentas e histórico de posições. |
| **Event** | Classe abstrata base para todos os eventos (`Abyss` e `Tool`). |
| **Enums** | `Color`, `EventType`, `PlayerState` para tipificação forte de dados. |

### Diagrama UML

A estrutura e relações entre as classes podem ser visualizadas no diagrama abaixo:

![Diagrama UML](diagrama.png)

---

## 👥 Autoria

Projeto desenvolvido por **Duarte Martins (a22400561)** Unidade Curricular: *Linguagens de Programação 2* Licenciatura em Engenharia Informática / Aplicações Multimédia e Videojogos  
**Universidade Lusófona**