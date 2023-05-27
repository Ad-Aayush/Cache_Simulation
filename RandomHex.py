import numpy as np


def choice():
    options = ['0', '1']
    s = ''.join(np.random.choice(options, (32)))
    ans = hex(int(s, 2))
    if len(ans) < 10:
        cnt = 10 - len(ans)
        ans = ans + cnt*'0'
    return ans[2:]

list = np.array([])
for i in range(50):
    list = np.append(list, choice())
print(len(list))
list = np.append(list, list)
print(len(list))
list = np.append(list, list)
print(len(list))
list = np.append(list, list)
np.random.shuffle(list)
print(len(list))
with open("Hex_Random_Numbers.txt", "w") as file:
    file.write('\n'.join(list))
