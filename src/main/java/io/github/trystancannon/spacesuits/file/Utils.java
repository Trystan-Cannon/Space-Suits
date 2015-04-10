/*
 * The MIT License
 *
 * Copyright 2015 Trystan Cannon.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package io.github.trystancannon.spacesuits.file;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Contains several essential file related activities (reading, writing, etc.).
 * 
 * @author Trystan Cannon
 */
public final class Utils {
    
    /**
     * Reads the contents of the file at the given path, returning it line-by-
     * line in a <code>List</code>. Returns <code>null</code> if anything goes
     * wrong with the process.
     * 
     * @param filePath
     * @return All lines in the file, <code>null</code> if any failure occurs.
     */
    public static List<String> getFileContents(String filePath) {
        File file = new File(filePath);
        
        if (file.exists() && !file.isDirectory()) {
            try (Scanner fileScanner = new Scanner(file)) {
                List<String> lines = new ArrayList<>();
                
                while (fileScanner.hasNextLine()) {
                    lines.add(fileScanner.nextLine());
                }
                
                fileScanner.close();
                return lines;
            } catch (Exception failure) {}
        }
        
        return null;
    }
    
    /**
     * Writes the given lines to the file at the given path.
     * 
     * @param filePath
     * @param lines
     * 
     * @return <code>true</code> if successful.
     */
    public static boolean writeFile(String filePath, List<String> lines) {
        File file = new File(filePath);
        
        if (!file.exists()) {
            // Try to create the file if it doesn't exist:
            try {
                file.createNewFile();
            } catch (Exception failure) {
                return false;
            }
        }
        
        // Attempt to write the file:
        try (PrintWriter writer = new PrintWriter(file.getAbsolutePath(), "UTF-8")) {
            for (String line : lines) {
                writer.println(line);
            }
            
            writer.flush();
            writer.close();
        } catch (Exception failure) {
            return false;
        }
        
        return true;
    }
    
}
