# ImageJ command example using CSBDeep to execute pretrained networks

You can use this repository as a starting point to integrate pretrained TensorFlow network execution into your own ImageJ project.  

## How to export your model
In order to use a pretrained TensorFlow model with the ImageJ CSBDeep command, it has to be exported as a ZIP file. Check out [this method from the CSBDeep Python code](https://github.com/CSBDeep/CSBDeep/blob/7e8de81e90de7509fa289bd12eec68b12cc66550/csbdeep/utils/tf.py#L59-L119) as an example export which works with the CSBDeep ImageJ command. The meta data is optional and currently not used in ImageJ.
