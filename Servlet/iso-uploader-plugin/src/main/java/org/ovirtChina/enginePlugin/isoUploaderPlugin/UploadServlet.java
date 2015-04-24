package org.ovirtChina.enginePlugin.isoUploaderPlugin;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import org.ovirtChina.enginePlugin.isoUploaderPlugin.HttpUtils;
import org.ovirtChina.enginePlugin.isoUploaderPlugin.FlowInfo;
import org.ovirtChina.enginePlugin.isoUploaderPlugin.FlowInfoStorage;
import org.ovirtChina.enginePlugin.isoUploaderPlugin.CommandExecuter;

public class UploadServlet extends HttpServlet {

    public static final String UPLOAD_DIR = "uploads";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int flowChunkNumber = getFlowChunkNumber(request);


        FlowInfo info = getFlowInfo(request);

        RandomAccessFile raf = new RandomAccessFile(info.flowFilePath, "rw");

        //Seek to position
        raf.seek((flowChunkNumber - 1) * info.flowChunkSize);

        //Save to file
        InputStream is = request.getInputStream();
        long readed = 0;
        long content_length = request.getContentLength();
        byte[] bytes = new byte[1024 * 100];
        while(readed < content_length) {
            int r = is.read(bytes);
            if (r < 0)  {
                break;
            }
            raf.write(bytes, 0, r);
            readed += r;
        }
        raf.close();


        //Mark as uploaded.
        info.uploadedChunks.add(new FlowInfo.FlowChunkNumber(flowChunkNumber));

        // Print the number
        System.out.println("Chunk #" + flowChunkNumber + " of " + info.flowFilename + " has been uploaded.");

        if (info.checkIfUploadFinished()) { //Check if all chunks uploaded, and change filename
            FlowInfoStorage.getInstance().remove(info);
            response.getWriter().print("All finished.");

            list();

            System.out.println(info.flowFilename + " is completed.");

        } else {
            response.getWriter().print("Upload");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int flowChunkNumber = getFlowChunkNumber(request);

        FlowInfo info = getFlowInfo(request);

        if (info.uploadedChunks.contains(new FlowInfo.FlowChunkNumber(flowChunkNumber))) {
            response.getWriter().print("Uploaded."); //This Chunk has been Uploaded.
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private int getFlowChunkNumber(HttpServletRequest request) {
        return HttpUtils.toInt(request.getParameter("flowChunkNumber"), -1);
    }

    private FlowInfo getFlowInfo(HttpServletRequest request) throws ServletException {
        String base_dir = UPLOAD_DIR;

        int flowChunkSize          = HttpUtils.toInt(request.getParameter("flowChunkSize"), -1);
        long flowTotalSize         = HttpUtils.toLong(request.getParameter("flowTotalSize"), -1);
        String flowIdentifier      = request.getParameter("flowIdentifier");
        String flowFilename        = request.getParameter("flowFilename");
        String flowRelativePath    = request.getParameter("flowRelativePath");

        //DEBUG
        // System.out.println(
        // "base_dir = " + base_dir + "\n"
        // + "flowChunkSize = " + flowChunkSize + "\n"
        // + "flowTotalSize = " + flowTotalSize + "\n"
        // + "flowIdentifier = " + flowIdentifier + "\n"
        // + "flowFilename = " + flowFilename + "\n"
        // + "flowRelativePath = " + flowRelativePath
        // );
        //!DEBUG


        File flowFile = new File(base_dir, flowFilename);

        // Create the folder if it doesn't exist yet
        flowFile.getParentFile().mkdirs();

        //Here we add a ".temp" to every upload file to indicate NON-FINISHED
        String flowFilePath = flowFile.getAbsolutePath() + ".temp";

        FlowInfoStorage storage = FlowInfoStorage.getInstance();

        FlowInfo info = storage.get(flowChunkSize, flowTotalSize,
                flowIdentifier, flowFilename, flowRelativePath, flowFilePath);
        if (!info.vaild())         {
            storage.remove(info);
            throw new ServletException("Invalid request params.");
        }
        return info;
    }

  private void list(){
    CommandExecuter cmdExe = new CommandExecuter();
    cmdExe.list();
  }
}
