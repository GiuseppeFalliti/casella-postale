# Istruzioni per l'esecuzione del programma

## Descrizione
Il programma simula il comportamento di comunicazione tramite tre caselle di posta. Le caselle di posta sono utilizzate da 10 postini che inviano messaggi e da 3 lettori che leggono i messaggi inviati.

## Requisiti
- Java Development Kit (JDK) installato sul sistema

## Esecuzione
1. Assicurarsi di avere Java installato sul sistema.
2. Scaricare il file Application.java sul proprio computer oppure scaricare il codice da github.
3. Aprire un terminale o prompt dei comandi e navigare fino alla directory in cui è presente il file Application.java.
4. Compilare il file Application.java eseguendo il seguente comando:
    ```
    javac Application.java
    ```
5. Dopo la compilazione, eseguire il programma utilizzando il comando:
    ```
    java Application
    ```

## Struttura del codice:

    Classe principale Application:
        Estende la classe Thread per gestire l'avvio del programma come thread separato.
        Crea una finestra Swing con tre JTextArea che rappresentano le caselle di posta e un pulsante per avviare la simulazione.
        Nel metodo setupApp(), viene definito il layout dell'interfaccia e gestiti gli eventi del pulsante di avvio della simulazione.

    Classe interna Reader:
        Implementa l'interfaccia Runnable per eseguire il compito dei lettori dei messaggi.
        Dentro il metodo run(), legge ciclicamente i messaggi dalle caselle di posta e li visualizza nella JTextArea corrispondente.
        Utilizza la sincronizzazione su Application.class per garantire l'accesso sicuro alle risorse condivise (le caselle di posta).

Funzionamento del codice:

    All'avvio del programma, vengono create tre caselle di posta e un pulsante "Start Simulation".
    Quando viene premuto il pulsante, viene avviato un thread di simulazione che invia messaggi casuali alle caselle di posta.
    Durante la simulazione, i messaggi inviati vengono visualizzati in rosso nelle caselle di posta.
    nel mentre, vengono avviati tre thread lettori che leggono i messaggi dalle caselle di posta e li visualizzano in verde.
    La simulazione fino a 10 il numero di thread che devono inviare il messaggio.
    Alla fine della simulazione, viene visualizzato un messaggio di avviso.

Sincronizzazione:

    Per garantire l'accesso sicuro alle caselle di posta condivise tra i thread, viene utilizzata la sincronizzazione su Application.class all'interno del metodo run() della classe Reader.
    Questo assicura che un solo thread alla volta possa accedere e modificare le risorse condivise, evitando problemi di concorrenza.

