package geobras;

import static geobras.GeoBras.locaisInfo;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import geobras.entities.EditarInfo;
import geobras.entities.Locais;
import geobras.entities.getInfoTotal;
import geobras.entities.ordenarPosicoes;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/**
 *
 * @author Rayssa Alves <rayssaalves.go@gmail.com>
 */
public class FXMLDocumentController implements Initializable {

    private boolean isButtonPressed = false;
    Locais locais = new Locais();

    EditarInfo editarInfo = new EditarInfo();

    @FXML
    private Label label;

    @FXML
    private Label resultadoPib;

    @FXML
    private ComboBox<String> caixaPesquisa;

    @FXML
    private TextField codigoPesquisa;

    @FXML
    private Label infoLabel;

    @FXML
    private Label PIBperCapitaPosicao;

    @FXML
    private Label IDHgeralPosicao;

    @FXML
    private Label IDHeducacionalPosicao;

    @FXML
    private Label IDHlongevidadePosicao;

    @FXML
    private Label labelHoraAtual;

    @FXML
    private Label codigoMunicipio;

    @FXML
    private TextField microrregiao;

    @FXML
    private TextField estado;

    @FXML
    private TextField regiao;

    @FXML
    private TextField area;

    @FXML
    private TextField populacao;

    @FXML
    private TextField domicilios;

    @FXML
    private TextField pibTotal;

    @FXML
    private TextField idh;

    @FXML
    private TextField rendaMedia;

    @FXML
    private TextField rendaNominal;

    @FXML
    private TextField pea;

    @FXML
    private TextField idhEducacional;

    @FXML
    private TextField idhLongevidade;

    @FXML
    private TextField densidadeDemografica;

    @FXML
    private TextField pibPerCapita;

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    public void initialize() {
        caixaPesquisa.setItems(FXCollections.observableArrayList(GeoBras.municipiosInfo));
    }

    @FXML
    private void handleEditarPopulacao(ActionEvent event) {
        editarInfo.editarPopulacao();
    }

    @FXML
    private void handleGetMunicipio() {
        String municipio = locais.getMunicipio();
    }

    @FXML
    public void handleAtualizarHora() {
        LocalDateTime instante = LocalDateTime.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String horaAtual = instante.format(fmt);

        if (horaAtual != null) {
            labelHoraAtual.setText("Última atualização: " + horaAtual);
        } else {
            labelHoraAtual.setText("Hora da última atualização não encontrada.");
        }
    }

    private boolean showConfirmationDialog() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("GeoBras Analítico");
        alert.setHeaderText("Você tem certeza que quer limpar as informações?");
        alert.setContentText("Limpando os dados você definirá os valores para 0 (Zero)");

        ButtonType buttonTypeYes = new ButtonType("Sim");
        ButtonType buttonTypeNo = new ButtonType("Não");

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == buttonTypeYes;
    }

    public static Locais calcularClassificacao(Double IDH, Double IDHeducacional, Double IDHLongevidade) {

        List<Locais> municipios = GeoBras.municipios;
        int classePIBPerCapita = 1, classeIDHGeral = 1, classeIDHEducacao = 1, classeIDHLongevidade = 1;
        for (int j = 0; j < municipios.size(); j++) {
            Locais municipioDois = municipios.get(j);

            if (IDH < municipioDois.getIDH()) {
                classeIDHGeral++;
            }
            if (IDHeducacional< municipioDois.getIDHeducacional()) {
                classeIDHEducacao++;
            }
            if (IDHLongevidade < municipioDois.getIDHlongevidade()) {
                classeIDHLongevidade++;
            }
        }
        Locais municipio = new Locais();
        municipio.setClasseIDHGeral(classeIDHGeral);
        municipio.setClasseIDHEducacao(classeIDHEducacao);
        municipio.setClasseIDHLongevidade(classeIDHLongevidade);
        
        return municipio;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        caixaPesquisa.getItems().addAll(GeoBras.getMunicipiosInfo());
        caixaPesquisa.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                barChart.getData().clear();
                System.out.println("Selecionado: " + newValue);
                String infoCodigo = getInfoTotal.getInfoForCodigo(newValue);
                String infoMicrorregiao = getInfoTotal.getInfoForMicrorregiao(newValue);
                String infoEstado = getInfoTotal.getInfoForEstado(newValue);
                String infoRegiao = getInfoTotal.getInfoForRegiao(newValue);
                String infoArea = getInfoTotal.getInfoForArea(newValue);
                String infoPopulacao = getInfoTotal.getInfoForPopulacao(newValue);
                String infoDomicilios = getInfoTotal.getInfoForDomicilios(newValue);
                String infoPIB = getInfoTotal.getInfoForPibTotal(newValue);
                String infoIDH = getInfoTotal.getInfoForIDH(newValue);
                String infoRendaMedia = getInfoTotal.getInfoForRendaMedia(newValue);
                String infoRendaNominal = getInfoTotal.getInfoForRendaNominal(newValue);
                String infoPEA = getInfoTotal.getInfoForPEA(newValue);
                String infoIDHeducacional = getInfoTotal.getInfoForIDHeducacional(newValue);
                String infoIDHlongevidade = getInfoTotal.getInfoForIDHlongevidade(newValue);
                String infoDensidade = getInfoTotal.getInfoForDensidade(newValue);
                String infoPibPerCapita = getInfoTotal.getInfoForPibPerCapita(newValue);
                
                Locais local = calcularClassificacao(Double.parseDouble(converterVirgulaParaPonto(infoIDH)), Double.parseDouble(converterVirgulaParaPonto(infoIDHeducacional)), Double.parseDouble(converterVirgulaParaPonto(infoIDHlongevidade)));

                codigoMunicipio.setText(infoCodigo);
                microrregiao.setText(infoMicrorregiao);
                estado.setText(infoEstado);
                regiao.setText(infoRegiao);
                area.setText(infoArea);
                populacao.setText(infoPopulacao);
                domicilios.setText(infoDomicilios);
                pibTotal.setText(infoPIB);
                rendaMedia.setText(infoRendaMedia);
                rendaNominal.setText(infoRendaNominal);
                pea.setText(infoPEA);
                densidadeDemografica.setText(infoDensidade);
                pibPerCapita.setText(infoPibPerCapita);
                IDHgeralPosicao.setText(local.getClasseIDHGeral() +"° ");
                IDHeducacionalPosicao.setText(local.getClasseIDHEducacao()+"° ");
                IDHlongevidadePosicao.setText(local.getClasseIDHLongevidade()+"° ");

                double idhValue = Double.parseDouble(infoIDH.replace(",", "."));
                double idhEducacionalValue = Double.parseDouble(infoIDHeducacional.replace(",", "."));
                double idhLongevidadeValue = Double.parseDouble(infoIDHlongevidade.replace(",", "."));

                try {
                    atualizarGraficoIDH(idhValue, idhEducacionalValue, idhLongevidadeValue);
                } catch (NumberFormatException e) {
                    System.out.println("Erro ao converter os valores: " + e.getMessage());
                }
            }
        });
    }

    public static void setTextFieldDouble(TextField txt) {
        txt.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && !newValue.matches("\\d*(\\.\\d*)?")) {
                txt.setText(oldValue);
            }
        });
    }

    @FXML
    public void handleZerarCampos() {
        boolean shouldClear = showConfirmationDialog();

        if (shouldClear) {
            area.setText("0");
            populacao.setText("0");
            domicilios.setText("0");
            pibTotal.setText("0");
            rendaMedia.setText("0");
            rendaNominal.setText("0");
            pea.setText("0");
            densidadeDemografica.setText("0");
            pibPerCapita.setText("0");
            PIBperCapitaPosicao.setText("0");
            IDHgeralPosicao.setText("0");
            IDHeducacionalPosicao.setText("0");
            IDHlongevidadePosicao.setText("0");
            barChart.getData().clear();
        }
    }


    private void atualizarGraficoIDH(double idhValue, double idhEducacionalValue, double idhLongevidadeValue) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        series.getData().add(new XYChart.Data<>("IDH", idhValue));
        series.getData().add(new XYChart.Data<>("IDH Educacional", idhEducacionalValue));
        series.getData().add(new XYChart.Data<>("IDH Longevidade", idhLongevidadeValue));

        barChart.getData().addAll(series);

        for (XYChart.Data<String, Number> data : series.getData()) {
            String categoria = data.getXValue();
            double valor = data.getYValue().doubleValue();
            String cor = getColorForValue(categoria, valor);
            data.getNode().setStyle("-fx-bar-fill: " + cor + ";");
        }
    }

    private String getColorForValue(String categoria, double valor) {
        if (categoria.equals("IDH")) {
            if (valor < 0.55) {
                return "#B12C25";
            } else if (valor >= 0.55 && valor < 0.70) {
                return "#eed545";
            } else if (valor >= 0.70 && valor < 0.80) {
                return "#40AE40";
            } else if (valor >= 0.80) {
                return "#3576A1";
            }
        } else if (categoria.equals("IDH Educacional") || categoria.equals("IDH Longevidade")) {
            if (valor < 0.55) {
                return "#B12C25";
            } else if (valor >= 0.55 && valor < 0.70) {
                return "#eed545";
            } else if (valor >= 0.70 && valor < 0.80) {
                return "#40AE40";
            } else if (valor >= 0.80) {
                return "#3576A1";
            }
        }

        return "#808080";
    }
    
    public static String converterVirgulaParaPonto(String numero) {
        if (numero == null) {
            return null;
        }
        return numero.replace(",", ".");
    }
}
