package com.alan344.controller;

import com.alan344.bean.DataItem;
import com.alan344.constants.StageConstants;
import com.alan344.init.DataSourceTreeItemInit;
import com.alan344.init.DataSourceTreeViewInit;
import com.alan344.init.MapperCheckBoxInit;
import com.alan344.utils.DialogUtils;
import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author AlanSun
 * @date 2019/8/7 17:04
 */
@Slf4j
@Controller
public class MainController implements Initializable {
    /**
     * 主布局控件
     */
    @Getter
    @FXML
    private BorderPane borderPane;

    /**
     * 右边的 border
     */
    @Getter
    @FXML
    private BorderPane borderPane1;

    @FXML
    private MenuBar menuBar;

    /**
     * 左边的 treeView
     */
    @Getter
    @FXML
    private TreeView<DataItem> treeViewDataSource;

    /**
     * 根数据源
     */
    @Getter
    @FXML
    private TreeItem<DataItem> treeItemDataSourceRoot;

    /**
     * 搜索小时用的 TextField
     */
    @Getter
    @FXML
    private TextField tableFindTextField;

    /**
     * 右边 border 固定再上面的 两个 HBox。存放 checkBox
     */
    @FXML
    private HBox mapperCheckBoxHBox1;

    @FXML
    private HBox mapperCheckBoxHBox2;

    //-------------------------------service----------------------------------------------------------------------------

    @Resource
    private DataSourceController dataSourceController;

    @Resource
    private ConfigController configController;

    @Resource
    private AboutController aboutController;

    @Resource
    private BeanFactory beanFactory;

    private HostServices hostServices;

    // -------------------------init----------------------------------------------------------------------------------//
    @Resource
    private DataSourceTreeItemInit dataSourceTreeItemInit;

    @Resource
    private DataSourceTreeViewInit dataSourceTreeViewInit;

    @Resource
    private MapperCheckBoxInit mapperCheckBoxInit;
    //--------------------------------init start----------------------------------------------------------------------//

    @SneakyThrows
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hostServices = beanFactory.getBean(HostServices.class);
        StageConstants.primaryStage = beanFactory.getBean(Stage.class);

        // 把菜单的长度和主布局控件绑定
        menuBar.prefWidthProperty().bind(borderPane.widthProperty());

        dataSourceTreeViewInit.treeViewInit(treeViewDataSource);

        // init mapperCheckBox
        mapperCheckBoxInit.checkBoxInit(mapperCheckBoxHBox1, mapperCheckBoxHBox2);

        // 从文件加载数据源至pane
        dataSourceTreeItemInit.initLoadData(treeItemDataSourceRoot);
        // 添加表搜索监听
        dataSourceTreeItemInit.addListenOnDataSourceBorderPane(treeViewDataSource);

        FXMLLoader fxmlLoader = new FXMLLoader();

        fxmlLoader.setLocation(getClass().getResource("/fxml/component/vbox-context-menu.fxml"));
        fxmlLoader.setControllerFactory(beanFactory::getBean);

        ListView<HBox> rightListViewInit = fxmlLoader.load();
        borderPane1.setCenter(rightListViewInit);
    }

    //--------------------------------init end------------------------------------------------------------------------//

    /**
     * 添加数据源
     */
    @FXML
    public void addSource() throws IOException {
        dataSourceController.addDataSource(StageConstants.primaryStage);
    }

    /**
     * 退出时弹出询问
     */
    @FXML
    public void exit() {
//        Platform.exit(); //直接退出
        DialogUtils.closeDialog(StageConstants.primaryStage);
    }

    /**
     * 点开项目地址
     */
    @FXML
    public void openGithub() {
        hostServices.showDocument("https://github.com/alansun2/mapper-generator-javafx");
    }

    /**
     * 导出窗口
     *
     * @throws IOException e
     */
    @FXML
    public void openConfigWindow() throws IOException {
        configController.openConfigPane(StageConstants.primaryStage);
    }

    /**
     * 关于窗口
     *
     * @throws IOException e
     */
    @FXML
    public void openAboutWindow() throws IOException {
        aboutController.openWindow(StageConstants.primaryStage);
    }
}
