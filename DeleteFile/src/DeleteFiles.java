
import java.io.File;


public class DeleteFiles {
	static File filePath = null;
	public static void main(String[] args) {
		String f1Path = "aaaa";
		String path = System.getProperty("user.dir")+"/"+f1Path;
		filePath = new File(path);
		System.out.println("filePath== "+filePath.getPath()+"\nabs path=="+filePath.getAbsolutePath()+"\n\n");
		deleteAll(filePath);
	}
	private static void deleteAll(File f1) {
		System.out.println("f1.exists(): "+f1.exists());
		boolean isF1Directory = f1.isDirectory();
		boolean isF1File = f1.isFile();
		System.out.println("isF1Directory"+isF1Directory+"isF1File"+isF1File);
		if (f1.exists()) {
			if (isF1Directory) {
				File files[] = f1.listFiles();
				for (File file : files) {
					if (file.isFile()) {
						file.delete();
					}
					if (file.isDirectory() && file.listFiles() == null) {
						System.out.println("empty Files");
						file.delete();
					} else {
						String subFilePath = file.getAbsolutePath();
						File subFile = new File(subFilePath);
						deleteAll(subFile);
					}
					file.delete();
				}
			}
			f1.delete();
			System.out.println("Delete "+f1.getAbsolutePath()+" successful.");
		} else {
			System.out.println("f1.==="+f1.getAbsolutePath());
			System.out.println("This file do not exists.");
		}

	}

}
