O objetivo deste projeto é o desenvolvimento de competências de programação que permita desenvolver, em colaboração, aplicações multiparadigma interativas de média escala. O projeto divide-se em duas partes. A primeira parte corresponde ao desenvolvimento da camada de negócio utilizando, obrigatoriamente, a linguagem de programação **Scala** seguindo os princípios de programação funcional pura (exceções serão explicitamente indicadas). A segunda parte corresponde ao desenvolvimento da camada de apresentação (interativa) que deverá ser desenvolvida utilizando a linguagem de programação **JavaFX**. Apesar de poder ser necessária para o projeto, as características de implementação da camada de dados não serão consideradas em termos de avaliação.

#### **Parte 1: Camada de Negócio**

**Objetivos:** Desenvolver um conjunto de métodos, utilizando o paradigma de programação funcional, capazes de garantir os objetivos funcionais da solução proposta. Para além do código fonte (em Scala) é necessário desenvolver o diagrama de classes representando a arquitetura da solução proposta.

#### **Parte 2: Camada de Apresentação Interativa**

**Objetivos:** Desenvolver uma *Graphical User Interface* que permite uma interação fácil e intuitiva com a aplicação.

#### **Requisitos**

O projeto deverá, adicionalmente, satisfazer os seguintes requisitos:

**Funcionais**

1. Permitir executar com sucesso as várias funcionalidades;
2. Manter estado entre execuções.

**Não funcionais**

1. Apresentar ambas interfaces: gráfica (*Graphical User Interface – GUI*) e textual (*text-based User Interface*);

2. Os utilizadores deverão conseguir interagir corretamente (com o menor número de erros cometidos) e com facilidade (equilíbrio adequado entre quantidade de informação apresentada e número de ações necessárias para realizar uma tarefa) sem necessidade de treino.

#### Tema

Neste trabalho prático ocupar-nos-emos com a aplicação de *quadtrees* na representação e processamento de imagens e, na criação de um álbum de imagens.
Existem dois métodos essenciais para a representação de imagens a cores:

1. Como bitmaps, matrizes bidimensionais contendo em cada posição informação relativa a um pixel (poderá ser um booleano (bit) caso se trate de uma imagem a preto e branco; um valor inteiro caso se trate de uma imagem em *grayscale* ou uma lista de 3 inteiros no caso das imagens a cores).
2. Como mapas vetoriais, sendo então cada imagem descrita por uma lista dos objetos que a constituem (por exemplo, quadrado de cor azul de lado 30 e coordenadas (20, 10) para o vértice inferior esquerdo).

As *quadtrees* proporcionam um terceiro método: uma otimização dos bitmaps em que se representa de forma atómica informação relativa a todo um bloco (retangular) de pixels. A ideia é considerar a imagem (que se considera ter uma forma retangular) dividida em quatro quadrantes iguais; cada um destes é em si uma imagem retangular que se divide de igual forma. A imagem assim dividida corresponde a um nó da *quadtree*, em que se armazena informação geométrica, nomeadamente as coordenadas de dois vértices opostos. Os seus descendentes são (as árvores correspondentes a) os seus 4 quadrantes. Quadrantes (ou secções) constituídos apenas por pixels da mesma cor não precisam de ser divididos: basta representá-los por folhas da árvore, onde é guardada informação relativa à cor. Naturalmente, em imagens com grandes manchas da mesma cor, é possível poupar espaço considerável por comparação com a representação por bitmap. No caso limite, a secção mais pequena é um pixel.

#### Tarefas

Pretende-se escrever os seguintes métodos:
**T1.** makeQTree(b:BitMap):QTree criação de uma *quadtree* a partir de um bitmap fornecido e método oposto i.e. para transformar uma quadtree num bitmap;
**T2.** scale(scale:Double, qt:QTree):QTree operação de ampliação/redução de uma imagem, segundo o fator fornecido (por exemplo 1.5 ampliará a imagem aumentando ambos os seus lados em 50%);
**T3.** mirrorV / mirrorH (qt:QTree):QTree operações de espelhamento vertical e horizontal;
**T4.** rotateD / rotateR (qt:QTree):QTree operações de rotação de 90 graus nos dois sentidos;
**T5.** mapColourEffect(f:Colour => Colour, qt:QTree):QTree mapeamento uniforme de uma função em toda a imagem. Deverá utilizar este método para ilustrar a aplicação dos efeitos *Noise*, *Contrast* e *Sepia*.

e ser possível realizar as operações típicas (**T6**) de um álbum de imagens (i.e., adicionar, remover, percorrer, procurar, trocar ordem das imagens, editar informação associada) e permitir diferentes formas de visualização (**T7**) (p.e. “*slideshow*”, grelha, etc.). Incluir T7 somente na *GUI*.

#### Alguns Tipos de Dados a Utilizar

```scala
type Point = (Int, Int)
type Coords = (Point, Point)
type Section = (Coords, Color)
trait QTree[+A]
case class QNode[A](value: A,
					one: QTree[A], two: QTree[A],
					three: QTree[A], four: QTree[A]) extends QTree[A]
case class QLeaf[A, B](value: B) extends QTree[A]
case object QEmpty extends QTree[Nothing]
```

A cada nó e cada secção (folha) de uma árvore estão associadas duas coordenadas, dos vértices superior esquerdo e inferior direito. Observe-se que a presença do *QEmpty* se deve à eventual necessidade de dividir imagens com apenas dois pixels em quadrantes; neste caso dois dos quadrantes serão vazios.

#### Material Fornecido

- ImageUtil.java – contendo métodos utilitários para lidar com imagens e cores (útil para ilustrar a transparência de integração destes dois paradigmas). Único código Java do projeto juntamente com uso do java.io.FileInputStream;
- objc2_2.png – imagem com 4 pixéis (dimensão 2 por 2) que pode ser utilizada para os testes iniciais. O método readColorImage da classe ImageUtil.java pode ser utilizado para transformar a imagem em Array[Array[Int]] e, posteriormente, transformado em List[List[Int]] antes de ser transformada numa *QTree*. O método writeImage da classe ImageUtil.java pode ser utilizado para gravar a matriz de Int de volta ao formato png;
- info.txt – contendo um exemplo de uma QTree[Coords].

#### Exemplo de Divisão de uma Imagem em Quadrantes

A raiz da árvore representa a imagem representada via *quadtree*.

![image-20210401144522893](C:\Users\bphsa\AppData\Roaming\Typora\typora-user-images\image-20210401144522893.png)

Figura 1 – Representação gráfica e respetiva representação em árvore simplificada de um *quadtree*