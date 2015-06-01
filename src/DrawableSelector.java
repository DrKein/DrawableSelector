import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by kein on 15. 5. 31..
 */
public class DrawableSelector extends AnAction {
    public void actionPerformed(AnActionEvent event) {
        if(event == null) return;

        VirtualFile[] fileArray = event.getData(DataKeys.VIRTUAL_FILE_ARRAY);
        if(fileArray == null || fileArray.length < 2) {
            return;
        }

        File path = new File(fileArray[0].getParent().getCanonicalPath());
        System.out.println("get parent : " + path.getAbsolutePath());

        String selectorName = null;
        String normal = null, pressed = null, selected = null, checked = null, disabled = null;
        for(int i=0 ; i<fileArray.length ; i++) {
            VirtualFile file = fileArray[i];
            String name = file.getNameWithoutExtension();
            if(name.endsWith(".9")) {
                name = name.substring(0, name.length()-2);
            }
            System.out.println("get name : " + name);

            String endString = name.substring(name.lastIndexOf("_"));
            System.out.println("end with : " + endString);

            // normal
            if(endString.startsWith("_n")) {
                normal = name;
                name = name.substring(0, name.lastIndexOf("_"));
                selectorName = name + "_selector.xml";
            }
            // pressed
            else if(endString.startsWith("_p")) {
                pressed = name;
            }
            // selected
            else if(endString.startsWith("_s")) {
                selected = name;
            }
            // checked
            else if(endString.startsWith("_c")) {
                checked = name;
            }
            // disabled
            else if(endString.startsWith("_d")) {
                disabled = name;
            }
        }

        if(selectorName == null) {
            System.out.println("selector name is null. (no normal file)");
            return;
        }

        // make selector.xml
        File xml = new File(path, selectorName);
        System.out.println("selector name : " + xml.getAbsolutePath());

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(xml));
            bw.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            bw.write(System.lineSeparator());
            bw.write("<selector xmlns:android=\"http://schemas.android.com/apk/res/android\">");
            bw.write(System.lineSeparator());
            if(pressed != null) {
                bw.write("<item android:state_pressed=\"true\"  android:drawable=\"@drawable/"+ pressed + "\" />");
                bw.write(System.lineSeparator());
            }
            if(checked != null) {
                bw.write("<item android:state_checked=\"true\"  android:drawable=\"@drawable/"+ checked + "\" />");
                bw.write(System.lineSeparator());
            }
            if(selected != null) {
                bw.write("<item android:state_selected=\"true\"  android:drawable=\"@drawable/"+ selected + "\" />");
                bw.write(System.lineSeparator());
            }
            if(disabled != null) {
                bw.write("<item android:state_enabled=\"false\"  android:drawable=\"@drawable/"+ disabled + "\" />");
                bw.write(System.lineSeparator());
            }

            bw.write("<item android:drawable=\"@drawable/"+ normal + "\" />");
            bw.write(System.lineSeparator());
            bw.write("</selector>");

            bw.flush();
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
