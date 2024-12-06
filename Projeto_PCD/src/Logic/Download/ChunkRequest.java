package Logic.Download;

import java.io.Serializable;

import Logic.Utils.FileInfo;

public class ChunkRequest implements Serializable{
        public static final int CHUNK_MAX_SIZE = 10000;
        private FileInfo fileInfo;
        private int offset;


    //Contrutor
        public ChunkRequest(FileInfo fileInfo, int offset){
            this.fileInfo=fileInfo;
            this.offset=offset;
        }










    //Getters
        public FileInfo getFileInfo() {return fileInfo;}
        public int getOffset() {return offset;}
        
}
