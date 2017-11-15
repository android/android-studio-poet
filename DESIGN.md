This is document that explains design, contracts and responsibilities of different classes. 
If you see that something is misaligned between this document and actual code, feel free to file an issue.

##Global architecture

There are mainly three big pieces in the puzzle:

* Input - this includes UI design, input format and reading input from the file(latter is not done yet).
* Processing - main goal of this layer is to generate blueprints for the generation. 
That includes but not limited:
    1. Decisions about what modules should be generated and how bee should they be, 
    2. Is there "main" module or not, is it an android module or not.
    3. Is input valid? For example, is there cycle dependencies between module graph.
* Generation - at this stage code gets generated according to information in blueprints.

##Generator's contract
Generator are used to turn blueprint into a different pieces of result project.
In short `Generator` takes a `Blueprint` as input, generate the code/files and returns a `GenerationResult`.

Important notice: `Generator` shouldn't interact with file system directly(aka shouldn't have dependency on `File` class), 
instead it should calls proper methods on `FileWriter`. When it needs to combine file paths, 
it shouldn't do it directly, instead it should use `joinPath` extension function.

Using `Blueprint` as input allows us to easier combine different generators together. 
In some cases it is ok to pass parent's blueprint to the child.

`GenerationResult` makes "sibling" dependencies between generators clearer and
let us easier pass information from one generator to another. For example,
having passing `StringResourceGenerationResult` instead of `List<String>` 
makes code easier to understand. Plus it will later allows us to do better error handling. 


 
 



