package net.zhengtianyi;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;


/**
 * 类名 ClassName  Screenshots
 * 项目 ProjectName  JavaFXScreenshots
 * 作者 Author  郑添翼 Taky.Zheng
 * 邮箱 E-mail 275158188@qq.com
 * 时间 Date  2019-05-29 18:52 ＞ω＜
 * 描述 Description TODO
 */
public class Screenshots extends Application {



    private Stage primaryStage; //将窗口设置为全局变量便于使用
    private Button btn; //点击截屏按钮
    private ImageView imageView;    //显示截取后的图片

    private Stage stage = new Stage(); //拖拽窗口
    private AnchorPane an = new AnchorPane(); //截屏背景
    private Scene scene = new Scene(an); //设置场景

    private Screen screen = Screen.getPrimary();    //获取屏幕信息
    private double screenWidth; //记录屏幕宽
    private double screeHeight; //记录屏幕高

    private double start_x; //记录截图开始x
    private double start_y; //记录截屏开始y

    private double end_x; //记录截图结束x
    private double end_y; //记录截屏结束y

    private HBox hBox = new HBox();  //用于拖拽框
    private Label label = new Label(); //拖拽大小提示框,用于显示截图大小
    private Button finishBtn = new Button("完成截图"); //完成截图按钮

    private double real_x; //拖拽实时坐标x
    private double real_y; //拖拽实时坐标y

    @Override
    public void start(Stage primaryStage) throws Exception {

        //将主窗口传出,作为全局变量方便使用
        this.primaryStage = primaryStage;

        //初始化界面
        initView();
        //初始化事件
        initEvent();

    }


    /**
     * 方法名 MethodName initView
     * 参数 Params []
     * 返回值 Return void
     * 作者 Author 郑添翼 Taky.Zheng
     * 编写时间 Date 2019-05-29 19:01 ＞ω＜
     * 描述 Description TODO 初始化界面
     */
    private void initView(){

        //初始化主窗口
        imageView = new ImageView();
        btn = new Button("点击截屏");
        VBox root = new VBox(10,btn, imageView);
        root.setPadding(new Insets(10));

        //初始化拖拽窗口
        //stage.initOwner(primaryStage);
        an.setStyle("-fx-background-color: #ffffff11");
        an.setTranslateY(-23);
        stage.setScene(scene);
        screenWidth = screen.getBounds().getWidth();
        screeHeight = screen.getBounds().getHeight();
        stage.setWidth(screenWidth);
        stage.setHeight(screeHeight);
        scene.setFill(Color.valueOf("#00000030"));
        stage.initStyle(StageStyle.TRANSPARENT);


        //给他拖拽窗口设置样式
        hBox.setStyle("-fx-background-color: #ffffff00; -fx-border-width: 1; -fx-border-color: #ff0000");
        label.setStyle("-fx-background-color: #000000; -fx-text-fill: #ffffff");


        primaryStage.setScene(new Scene(root));
        primaryStage.setWidth(600);
        primaryStage.setHeight(600);
        primaryStage.setTitle("截屏工具1.0");
        primaryStage.show();

    }

    /**
     * 方法名 MethodName initEvent
     * 参数 Params []
     * 返回值 Return void
     * 作者 Author 郑添翼 Taky.Zheng
     * 编写时间 Date 2019-05-29 19:01 ＞ω＜
     * 描述 Description TODO 初始化事件
     */
    private void initEvent(){


        //给点击截图按钮设置事件
        btn.setOnAction(p ->{
            an.getChildren().clear();
            //隐藏主窗口
            primaryStage.hide();
            stage.show();
        });

        //设置end退出截屏窗口
        scene.setOnKeyPressed(p -> {
            if (p.getCode() == KeyCode.ESCAPE) {
                stage.close();
                primaryStage.show();
            }
        });

        //设置点击事件
        an.setOnMousePressed(p ->{

            //清空拖拽窗口控件的数据,以便重新设置
            an.getChildren().clear();
            hBox.setPrefWidth(0);
            hBox.setPrefHeight(0);
            label.setText("宽度: 0 高度: 0");

            //记录拖拽开始坐标
            start_x = p.getSceneX();
            start_y = p.getSceneY();

            //给拖拽框设置起始位置
            AnchorPane.setLeftAnchor(hBox, start_x);
            AnchorPane.setTopAnchor(hBox, start_y);

            //给信息提示框设置位置
            label.setLayoutX(start_x);
            label.setLayoutY(start_y - label.getHeight());

            //给拖拽窗口添加元素
            an.getChildren().add(hBox);
            an.getChildren().add(label);

            //添加完成截屏按钮,默认开始不显示
            finishBtn.setVisible(false);
            an.getChildren().add(finishBtn);

        });

        //设置拖拽检测
        an.setOnDragDetected(dragDetected -> an.startFullDrag());

        //设置拖拽事件
        an.setOnMouseDragOver(p -> {
            //获取实时拖拽的坐标
            end_x = p.getSceneX();
            end_y = p.getSceneY();

            //获取拖拽实时大小
            real_x = end_x - start_x;
            real_y = end_y - start_y;

            //给拖拽框设置大小
            hBox.setPrefWidth(real_x);
            hBox.setPrefHeight(real_y);

            //给提示框设置大小信息
            label.setText("宽度: " + real_x + " 高度: " + real_y);

        });

        //设置拖拽结束事件
        an.setOnMouseDragReleased(p ->{

            //设置完成截屏按钮位置
            AnchorPane.setLeftAnchor(finishBtn,end_x - finishBtn.getWidth());
            AnchorPane.setTopAnchor(finishBtn,end_y - finishBtn.getHeight());
            //结束是显示完成截图按钮
            finishBtn.setVisible(true);

        });

        //设置完成按钮事件
        finishBtn.setOnAction(p ->{
            try {
                getScreenImage();
            } catch (Exception e) {
                e.printStackTrace();
            }

        });


    }


    /**
     * 方法名 MethodName getScreenImage
     * 参数 Params []
     * 返回值 Return void
     * 作者 Author 郑添翼 Taky.Zheng
     * 编写时间 Date 2019-05-30 12:43 ＞ω＜
     * 描述 Description TODO 获取图片
     */
    private void getScreenImage() throws Exception {

        stage.close();

        Robot robot = new Robot();
        //创建一个矩形,使用截屏工具根据位置截取出矩形大小的图片
        Rectangle rectangle = new Rectangle((int)start_x,(int)start_y + 22,(int)real_x,(int)real_y);
        BufferedImage bufferedImage = robot.createScreenCapture(rectangle);

        //获取图片,并放置到ImageView中
        WritableImage writableImage = SwingFXUtils.toFXImage(bufferedImage, null);
        imageView.setImage(writableImage);

        //获取系统剪切板,存入截图
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putImage(writableImage);
        clipboard.setContent(content);
        //输出到桌面
        ImageIO.write(bufferedImage, "png", new File("/Users/zhengtianyi/Desktop/image.png"));

        primaryStage.show();
    }







    /**
     * 方法名 MethodName main
     * 参数 Params [args]
     * 返回值 Return void
     * 作者 Author 郑添翼 Taky.Zheng
     * 编写时间 Date 2019-05-29 18:53 ＞ω＜
     * 描述 Description TODO 主函数
     */
    public static void main(String[] args) {
        launch(args);
    }
}
