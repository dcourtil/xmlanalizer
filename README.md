# Smart XML Analizer

### Prerequisites

You will need to have installed JRE 8.

## Getting Started

Run the following command to execute the Xml Analizer:
```
java -jar xmlanalizer.jar <input_origin_file_path> <input_other_sample_file_path> <target_element_id>
```
## Results
Folowwing are the output results of the excecutions with the provided samples.

### Execution 1
Command run:
```
java -jar xmlanalizer.jar ./examples/sample-0-origin.html ./examples/sample-1-evil-gemini.html make-everything-ok-button
```
Output result:
```
> html > body > div > div > div:eq(2)  > div > div > div:eq(1)  > a:eq(1) 
```
### Execution 2
Command run:
```
java -jar xmlanalizer.jar ./examples/sample-0-origin.html ./examples/sample-2-container-and-clone.html make-everything-ok-button
```
Output result:
```
> html > body > div > div > div:eq(2)  > div > div > div:eq(1)  > div > a
```
### Execution 3
Command run:
```
java -jar xmlanalizer.jar ./examples/sample-0-origin.html ./examples/sample-3-the-escape.html make-everything-ok-button
```
Output result:
```
 > html > body > div > div > div:eq(2)  > div > div > div:eq(2)  > a
```
### Execution 4
Command run:
```
java -jar xmlanalizer.jar ./examples/sample-0-origin.html ./examples/sample-4-the-mash.html make-everything-ok-button
```
Output result:
```
 > html > body > div > div > div:eq(2)  > div > div > div:eq(2)  > a
```
