package com.joao.emailservice;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;


import com.google.gson.Gson;
import com.joao.emailservice.models.Email;
import com.joao.emailservice.models.Node;

import org.json.JSONObject;
import org.json.*;



import static android.content.ContentValues.TAG;
import static com.joao.emailservice.utils.LinkedListUtils.removeEqualsFromHead;

public class EmailService extends IntentService {

    public static final String EMAIL_SERVICE_FINISHED = "com.joao.emailservice.EmailService.EMAIL_SERVICE_FINISHED";


    public EmailService() {
        super("EmailService");

    }

    /*
    Este é o codigo principal da aplicação/servico, Ele extende de IntentService , que cuida de orquestrar chamadas
    multiplas ao servico para sincronia.

    Ele tira da intent a lista linkada em JSON como String, desserializa e transforma em um Node ja linkado com o resto.

    Então chama uma função assíncrona, built in do android para executar a operação de remoçaõ.

    Executa a função de remoção(Mesma da questão 5) e entao chama um callback para retornar o resultado ao fluxo do servico.



    E por fim, retorna o resultado por meio de um broadcast, que deve estar registrado na outra aplicação

    */

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d(TAG,"Executando");

        String rawData = intent.getStringExtra("data");

        Log.d("RAW DATA",rawData);

        Node<Email> emails = new Node<>();
        Node<Email> tail = emails;

        try{
            JSONObject jo = new JSONObject(rawData);
            while(jo != null){
                JSONObject tmp = jo.getJSONObject("data");
                Email mail = new Email(tmp.getString("title"),tmp.getString("body"));
                tail.setData(mail);
                jo = (JSONObject) jo.get("next");

                tail.setNext(new Node());

                tail = tail.getNext();



            }

        }catch (Exception e){
            e.printStackTrace();
        }
        Log.d("DADOS:",emails.toString());

        new RemoveEqualsAsyncTask(new OnFinishRemoving() {
            @Override
            public void onFinishRemoving(Node n) {
                final Intent intent = new Intent(EmailService.EMAIL_SERVICE_FINISHED);


                Log.d("Apos conversão",n.toString());

                String serialized = new Gson().toJson(n);
                intent.putExtra("data",serialized);
                sendBroadcast(intent);


            }
        }
        ).execute(emails);

    }


    private interface OnFinishRemoving{
        void onFinishRemoving(Node n);
    }


    private static class RemoveEqualsAsyncTask extends AsyncTask<Node, Integer, Node>{

        OnFinishRemoving handler;

        private RemoveEqualsAsyncTask(OnFinishRemoving handler){
            this.handler = handler;
        }
        @Override
        protected Node doInBackground(Node... emails) {
            Log.d("Reduzindo","true");
            return removeEqualsFromHead(emails[0]);

        }

        @Override
        protected void onPostExecute(Node node) {
            super.onPostExecute(node);
            this.handler.onFinishRemoving(node);

        }
    }



}
