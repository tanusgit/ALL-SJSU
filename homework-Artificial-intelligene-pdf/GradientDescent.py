#!/usr/bin/env python
# coding: utf-8

# In[14]:


FirstX = 10 # pick a random x
learningRate = 0.1 #learning_rate
precisionRate = 0.000001 #error or precision 
stepSize = 1
iterations = 100 
count = 0
df = lambda x: (6*x)+2 #derivative function



while stepSize > precisionRate and count < iterations:
    prevX = FirstX 
    FirstX = FirstX - learningRate * df(prevX) #Go to the oposite direction
    stepSize = abs(FirstX - prevX)
    count = count+1 
   
print("The local minimum occurs at", FirstX)


# In[ ]:





# In[ ]:





# In[ ]:




