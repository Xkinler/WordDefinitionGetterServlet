# WordDefinitionGetterServlet
## What's this?
A servlet project that could respond with any English word's definitions along with other info in JSON

## Where does it come from?
Well... this project was unexpected at first... I began it with just an idea of me wanting to have an API to get the definitions of any English word...
But I then found it hard to find such an API (or strictly speaking, a REST API? I don't know for sure, lol). So I thought why not write one on my own...

So I started....

Within one day, I finished it, commited the code and ..... here's the repository hosting the code!!!!

## What can I do with this project?
Well, as I mentioned earlier in the previous section, you can simply get definitions of EVERY SINGLE ENGLISH WORD from this servlet. All you have to do is to set up the environment (I'll tell you how to in the section below :) ), send a request with either your code, or simply a browser, to the servlet, and you'll finally get a JSON format response packed with loads of information regarding the word you want. And then ENJOY IT!!!!

## How to set up/build/use/compile/...(Whatever term you wanna use) this project?
1. First of all, you need a **Tomcat Server** (I think most versions will work, I used version 9.0.37 during development). To download, here's the link (for Tomcat 9): https://tomcat.apache.org/download-90.cgi
 
 *For the rest of this section, since I used **IntelliJ IDEA Professional** during development, I'll use that as the IDE to run the project*
 
 2. Make sure you have **IntelliJ IDEA Professional** installed on your computer (either on a PC with Windows or a Mac)
 3. Download the code in this repository
 
    3.1 **Method 1**: Simply download the ZIP file from this repository
    
    3.2 **Method 2**: Using command line (or Terminal in macOS or Linux):
    
    3.2.1 Open your command line tool and `cd` to a directory where you want to place the code
    
    3.2.2 use the following command to fetch the code:
    
            git clone https://github.com/Xkinler/WordDefinitionGetterServlet.git
4. Start a new project in **IntellJ IDEA Professional** and import ALL code within the source code tree
5. Configure Tomcat in **IntellJ IDEA Professional**
6. Run the code
7. Within the browser, try adding `/getDefinition/<Any Word You Want>` to the URL to see the JSON response

## Want to Contribute?
Feel free to fork this repository and create a pull request! Thank you so much! As a newbie in open source community, I'd love to see other people getting involved into my project! It means a lot to me!

## Contact Me
Email: yu_jingbin@126.com or you can open an issue in this repository. I'll reply you as early as I can~

