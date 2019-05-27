package update;

import java.io.*;

/**
 * lqf -- 按照规范新建项目，按需要输入项目名称
 * */
public class CreateProjectTest {

    public static void main(String[] args) {
        CreateProjectTest createProjectTest = new CreateProjectTest();
        try {
            createProjectTest.traversalFolder("C:/主动加密文件夹(s0615)/phoenix/phoenix-sparrow","sparrow","druid");
        } catch (IOException e){
            e.printStackTrace();
        }
    }


    /**
     * 遍历文件夹,如果文件夹或者文件名含有字符串,则替换
     * */
    public void traversalFolder(String path, String oldName, String newName) throws IOException {
        // 直接执行修改文件名方法
        String newPath = updateName(path, oldName, newName);
        // 根据路径获取文件，根据是文件夹还是文件来操作
        File file = new File(newPath);
        // 文件夹
        if (file.isDirectory()) {
            // 察看文件夹下所有文件
            String[] fileList = file.list();
            for (String thisFileName : fileList){
                File thisFile = new File(newPath + "/" + thisFileName);
                // 如果文件以"."开头，则忽略
                if (thisFileName.substring(0,1).equals(".")){
                    System.out.println("已忽略" + thisFileName);
                    continue;
                }

                // 如果是个文件夹，递归
                if (thisFile.isDirectory()){
                    traversalFolder(newPath + "/" + thisFileName, oldName, newName);
                }
                // 是一个文件
                else {
                    // 修改文件名
                    String newThisFilePath = updateName(thisFile.getPath(), oldName, newName);
                    // 修改文件内容
                    replaceAllFileContext(newThisFilePath, oldName, newName);
                }
            }
        }
        // 是个文件
        else {
            // 修改文件内容，文件名一开始就修改了
            replaceAllFileContext(file.getPath(), oldName, newName);
        }
    }

    /**
     * 替换文件内容
     * */
    private void replaceAllFileContext(String path, String oldName, String newName) throws IOException {
        File file = new File(path);
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
        //内存流
        CharArrayWriter caw=new CharArrayWriter();

        //替换
        String line=null;

        //以行为单位进行遍历
        while((line = br.readLine()) != null){
            //替换每一行中符合被替换字符条件的字符串
            line = line.replaceAll(oldName, newName);
            //将该行写入内存
            caw.write(line);
            //添加换行符，并进入下次循环
            caw.append(System.getProperty("line.separator"));
        }
        //关闭输入流
        br.close();

        //将内存中的流写入源文件
        FileWriter fw=new FileWriter(file);
        caw.writeTo(fw);
        fw.close();
    }

    /**
     * 修改文件名
     * */
    private String updateName(String path, String oldName, String newName){
        // 根据路径获取文件，根据是文件夹还是文件来操作
        File file = new File(path);
        String fileName = file.getName();
        // 正则匹配将对应的字符串替换
        String fileNewName = fileName.replaceAll(oldName, newName);
        if (!fileName.equals(fileNewName)) {
            String fileParentPath = file.getParent();
            file.renameTo(new File(fileParentPath + "/" + fileNewName));
            return fileParentPath + "/" + fileNewName;
        }
        return file.getPath();
    }

}
