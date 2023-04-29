package com.example.appptin.medico.conexion;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.example.appptin.medico.fragments.historialPaciente.InformePaciente;
import com.example.appptin.medico.fragments.historialPeticion.PeticionClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

public class Conexion_json {

    private Context context;

    public Conexion_json(Context context) {
        this.context = context;
    }

    // Método para leer el JSON desde un archivo en la carpeta assets
    public String readJsonFromFile(String fileName) {
        String jsonString = null;

        try {
            InputStream inputStream = context.getAssets().open(fileName);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            jsonString = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonString;
    }

    // Método para obtener una lista de objetos desde el JSON
    public ArrayList<InformacionBase> getPedidosFromJson(String jsonString) {
        ArrayList<InformacionBase> pedidosList = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONObject idObject = jsonObject.getJSONObject("_id");
                String idValue = idObject.getString("$oid");
                String nombre = jsonObject.getString("nombre");
                String apellido = jsonObject.getString("apellido");
                String dni = jsonObject.getString("dni");
                String numeroPedido = jsonObject.getString("numero_pedido");
                JSONArray listaMedicamentos = jsonObject.getJSONArray("lista_medicamentos");

                ArrayList<String> medicamentosList = new ArrayList<>();
                for (int j = 0; j < listaMedicamentos.length(); j++) {
                    medicamentosList.add(listaMedicamentos.getString(j));
                }

                PeticionClass pedido = new PeticionClass(idValue,nombre, apellido, dni, numeroPedido, medicamentosList);
                pedidosList.add(pedido);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return pedidosList;
    }

    // Método para obtener una lista de objetos (InformePAciente) desde el JSON
    public ArrayList<InformacionBase> getInformePacientesFromJson(String jsonString) {
        ArrayList<InformacionBase> informesList = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                JSONObject idObject = jsonObject.getJSONObject("_id");
                String idValue = idObject.getString("$oid");

                String medico = jsonObject.getString("medico");

                JSONObject datosPersonalesObject = jsonObject.getJSONObject("datos_personales");
                String nombre = datosPersonalesObject.getString("nombre");
                String apellidos = datosPersonalesObject.getString("apellidos");
                String n_ident = datosPersonalesObject.getString("n_ident");
                String fechaNacimiento = datosPersonalesObject.getString("fecha_nacimiento");
                String genero = datosPersonalesObject.getString("genero");
                String cip = datosPersonalesObject.getString("cip");
                String pais = datosPersonalesObject.getString("pais");
                String provincia = datosPersonalesObject.getString("provincia");

                String antecedentesPersonales = jsonObject.getString("antecedentes_personales");
                String problemasSalud = jsonObject.getString("problemas_salud");
                String tratamientos = jsonObject.getString("tratamientos");

                InformePaciente informe = new InformePaciente(idValue, nombre, apellidos, n_ident,medico, fechaNacimiento, genero, cip, pais,
                                                           provincia, antecedentesPersonales, problemasSalud, tratamientos);
                informesList.add(informe);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return informesList;
    }





    // Método para escribir un JSON a partir de una lista de objetos
    public String writeJsonFromPedidos(ArrayList<PeticionClass> pedidosList) {
        JSONArray jsonArray = new JSONArray();

        try {
            for (int i = 0; i < pedidosList.size(); i++) {
                JSONObject jsonObject = new JSONObject();

                PeticionClass pedido = pedidosList.get(i);
                jsonObject.put("nombre", pedido.getNombre());
                jsonObject.put("apellido", pedido.getApellidos());
                jsonObject.put("dni", pedido.getDni());
                jsonObject.put("numero_pedido", pedido.getNumeroPedido());

                JSONArray listaMedicamentos = new JSONArray();
                for (int j = 0; j < pedido.getMedicamentosSize(); j++) {
                    listaMedicamentos.put(pedido.getMedicamentos().get(j));
                }
                jsonObject.put("lista_medicamentos", listaMedicamentos);

                jsonArray.put(jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonArray.toString();
    }

    public void deleteObjectJson(String jsonString, String delete_id, String fileName) throws JSONException, IOException {
        // Obtener el JSONArray
        JSONArray jsonArray = new JSONArray(jsonString);

        // Buscar el objeto con el id correspondiente
        int index = -1;

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if (jsonObject.has("_id") && jsonObject.getJSONObject("_id").getString("$oid").equals(delete_id)) {
                index = i;
                Log.d("JSON", "IDDDDDDDDD: "+jsonObject.getJSONObject("_id").getString("$oid"));

                break;
            }
        }

        // Eliminar el objeto si se encontró
        if (index >= 0) {
            jsonArray.remove(index);

            // Guardar los cambios en el archivo JSON en assets
            guardarJsonEnAssets(jsonArray, fileName);
        }
    }

    private void guardarJsonEnAssets(JSONArray jsonArray, String fileName) throws IOException, JSONException {

        // Obtener el AssetManager
        AssetManager assetManager = context.getAssets();

        // Obtener el archivo JSON como InputStream
        InputStream inputStream = assetManager.open(fileName);

        // Convertir el InputStream en String
        String jsonString = convertInputStreamToString(inputStream);

        // Convertir el String en JSONArray
        JSONArray originalJsonArray = new JSONArray(jsonString);

        // Reemplazar el JSONArray original con el nuevo
        originalJsonArray = jsonArray;

        // Convertir el JSONArray modificado en String
        String modifiedJsonString = originalJsonArray.toString();

        // Escribir los cambios en el archivo JSON en assets
        OutputStream outputStream = assetManager.openFd(fileName).createOutputStream();
        outputStream.write(modifiedJsonString.getBytes());
        outputStream.flush();
        outputStream.close();

    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line).append("\n");
        }
        inputStream.close();
        return stringBuilder.toString();
    }
}
