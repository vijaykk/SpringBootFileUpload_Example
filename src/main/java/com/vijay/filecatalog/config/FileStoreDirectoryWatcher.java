package com.vijay.filecatalog.config;


import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;
import java.nio.file.StandardWatchEventKinds;
public class FileStoreDirectoryWatcher 
{
	private WatchService watcher;
	
	public FileStoreDirectoryWatcher()
	{
		Init();
	}

	private void Init()
	{
		
		try {
            watcher = FileSystems.getDefault().newWatchService();
            Path dir = Paths.get("C:\\Users\\vijay\\Documents\\filecatalog\\store");
            dir.register(watcher, ENTRY_CREATE);
		}
		catch(IOException e)
		{
			
		}
		
	}

	public static void main(String s[])
	{
		FileStoreDirectoryWatcher watch = new FileStoreDirectoryWatcher();
		//watch.pollDirectory();
		
	    Path myDir = Paths.get("C:\\Users\\vijay\\Documents\\filecatalog\\store");       

        try {
           WatchService watcher = myDir.getFileSystem().newWatchService();
           myDir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE, 
           StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);

           WatchKey watckKey = watcher.take();

           List<WatchEvent<?>> events = watckKey.pollEvents();
           for (WatchEvent event : events) {
                if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
                    System.out.println("Created: " + event.context().toString());
                }
                if (event.kind() == StandardWatchEventKinds.ENTRY_DELETE) {
                    System.out.println("Delete: " + event.context().toString());
                }
                if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
                    System.out.println("Modify: " + event.context().toString());
                }
            }
           
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
	}
	public void pollDirectory()
	{
		             WatchKey key;
	                try {
	                    key = watcher.take();
	                } catch (InterruptedException ex) {
	                    return;
	                }
	                for (WatchEvent<?> event : key.pollEvents())
	                {
	                    WatchEvent.Kind<?> kind = event.kind();
	                     
	                    @SuppressWarnings("unchecked")
	                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
	                    Path fileName = ev.context();
	                     
	                     
	                    if (kind == ENTRY_CREATE )
	                    {
	                    	System.out.println(kind.name() + ": " + fileName);
		                    	                    }
	                }
	                 
	                boolean valid = key.reset();
	                
	       
		
	}
}
