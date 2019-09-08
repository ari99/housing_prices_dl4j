import pandas as pd
'''
	Script to help with creating DL4J Schemas. This outputs code which can be used to
	create DL4J categorical columns with their associated possible values.
'''

# Print the column and its associated index
def printIndexes(columnNames):
	index=0
	for columnName in columnNames:
	    print(str(index) + "  "+ columnName)
	    index = index+1
	 
# Creates a dictionary mapping column names to their possible values	 
def createValuesDict(data, columnNames):
	valuesDict={}
	for columnName in columnNames:
		valuesDict[columnName]=[]
		print(valuesDict[columnName])


	for row in data:
		for columnName in columnNames:
			value = row[columnName]
			if value not in valuesDict[columnName]:
				#print("Adding value " + str(value))
				valuesDict[columnName].append(value)
	return valuesDict

# Determine if the values contain an int and their min/max values
def createIntegerFeatureMinMaxs(values):
	min=10000000
	max=0
	intValues=False
	for value in values:
		if isinstance(value, int) or isinstance(value,float):
			intValues = True
			if(value<min):
				min=value
			if(value>max):
				max=value
	return intValues,min,max




data = pd.read_csv("train.csv").to_dict(orient="row")
columnNames = list(data[0].keys())
print(columnNames)
printIndexes(columnNames)

valuesDict = createValuesDict(data, columnNames)


minMaxes={}
for key in valuesDict:
	values = valuesDict[key]
	# If there is  a reasonably small amount of values then output the code
	#  for generating the categorical feature in DL4j
	if len(values) < 200:
		valueStr = ', '.join('"{0}"'.format(val) for val in values)
		print(".addColumnCategorical(\""+key+"\", Arrays.asList("+valueStr +"))")
	
	intValues,min,max = createIntegerFeatureMinMaxs(values)
				
	if intValues:
			minMaxes[key] = {'min':min, 'max':max}
	
print("MIN MAXES")
print(minMaxes)


     
     
