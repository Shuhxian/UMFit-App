#!/usr/bin/env python
# coding: utf-8

import surprise
import datetime
from surprise import Dataset
from surprise import Reader
from surprise.model_selection import GridSearchCV
from flask import Flask,request
from flask_restful import Resource, Api, reqparse
from flask_cors import CORS
import pandas as pd
import ast
import pickle
import re  

app = Flask(__name__)
api = Api(app)
CORS(app)

class Data(Resource):
    def get(self,userId,filename):
        if filename in ["feedback","rating","user"]:
            df=pd.read_csv(filename+".csv")
            df["userId"]=df["userId"].astype(str)
            if filename=="user" and userId not in df['userId'].unique():
                new_df = pd.DataFrame({"userId":userId,"targetHR":80,"maxHR":120,"duration":5,"frequency":3,"hrReserve5%":2,"lastUpdate":datetime.datetime.now().strftime("%d/%m/%Y %H:%M:%S")},index=[0])
                df = df.append(new_df, ignore_index=True)
                df.to_csv('user.csv', index=False)
            df=df[df["userId"]==userId]
            return df.reset_index(drop=True).to_dict(),200
        return {"Error":"File not found"},200

class Update(Resource):
    def get(self,userId,targetHR,maxHR,duration,frequency):
        df=pd.read_csv("user.csv")
        df["userId"]=df["userId"].astype(str)
        if userId in df['userId'].unique():
            df.loc[(df['userId']==userId),"targetHR"]=targetHR
            df.loc[(df['userId']==userId),"maxHR"]=maxHR
            df.loc[(df['userId']==userId),"duration"]=duration
            df.loc[(df['userId']==userId),"frequency"]=frequency
        else:
            new_df = pd.DataFrame({"userId":userId,"targetHR":targetHR,"maxHR":maxHR,"duration":duration,"frequency":frequency,"hrReserve5%":2,"lastUpdate":datetime.datetime.now().strftime("%d/%m/%Y %H:%M:%S")},index=[0])
            df = df.append(new_df, ignore_index=True)
        df.to_csv('user.csv', index=False)
        return True,200
    
class Feedback(Resource):
    def get(self,userId,wuComplete,wuDiff,wuEnjoy,ex1Type,ex1Complete,ex1Diff,ex1Enjoy,ex2Type,ex2Complete,ex2Diff,ex2Enjoy,
            ex3Type,ex3Complete,ex3Diff,ex3Enjoy,ex4Type,ex4Complete,ex4Diff,ex4Enjoy,cdComplete,cdDiff,cdEnjoy,
            chestPain,shortBreath,jointPain):
        df = pd.read_csv('rating.csv')
        df["userId"]=df["userId"].astype(str)
        default_exercise=["wu","ex1","ex2","ex3","ex4","cd"]
        exercise=["wu",ex1Type.replace("+"," "),ex2Type.replace("+"," "),ex3Type.replace("+"," "),ex4Type.replace("+"," "),"cd"]
        complete=[wuComplete,ex1Complete,ex2Complete,ex3Complete,ex4Complete,cdComplete]
        diff=[wuDiff,ex1Diff,ex2Diff,ex3Diff,ex4Diff,cdDiff]
        rating=[wuEnjoy,ex1Enjoy,ex2Enjoy,ex3Enjoy,ex4Enjoy,cdEnjoy]
        if userId in df['userId'].unique():
            rated=df[df["userId"]==userId].type.unique()
            for i in range(len(exercise)):
                if exercise[i] in ["Null","wu","cd"]:
                    continue
                if exercise[i] in rated:
                    df.loc[(df['userId']==userId) & (df['type']==exercise[i])]=rating[i]
                else:
                    new_df = pd.DataFrame({"userId":userId,"type":exercise[i],"rating":rating[i]},index=[0])
                    df = df.append(new_df, ignore_index=True)
        else:
            # create new dataframe containing new values
            for i in range(len(exercise)):
                if exercise[i] in ["Null","wu","cd"]:
                    continue
                new_df = pd.DataFrame({"userId":userId,"type":exercise[i],"rating":rating[i]},index=[0])
                df = df.append(new_df, ignore_index=True)
        df.to_csv('rating.csv', index=False)
        #Model retrain
        reader = Reader(rating_scale=(1, 5))
        data = Dataset.load_from_df(df, reader) 
        trainingSet = data.build_full_trainset()
        model = surprise.KNNBaseline(n_epochs=5,lr_all=0.001,reg_all=0.4)
        model.fit(trainingSet)
        pickle.dump(model, open("model.sav", 'wb'))
        #Finish model retrain
        #save data to feedback csv
        df=pd.read_csv("feedback.csv")
        df["userId"]=df["userId"].astype(str)
        data={"timestamp":datetime.datetime.now().strftime("%d/%m/%Y %H:%M:%S"),
             "userId":userId}
        for i in range(len(exercise)):
            if complete[i]=="true" or exercise[i]=="Null":
                data[default_exercise[i]+"Complete"]=0
            else:
                data[default_exercise[i]+"Complete"]=1
            data[default_exercise[i]+"Diff"]=diff[i]
            data[default_exercise[i]+"Enjoy"]=rating[i]
            if default_exercise[i]!="wu" and default_exercise[i]!="cd":
                data[default_exercise[i]+"Type"]=exercise[i]
        if chestPain=="false":
            data["chestPain"]=0
        else:
            data["chestPain"]=1
        if shortBreath=="false":
            data["shortBreath"]=0
        else:
            data["shortBreath"]=1
        if jointPain=="false":
            data["jointPain"]=0
        else:
            data["jointPain"]=1
        new_df = pd.DataFrame(data,index=[0])
        df = df.append(new_df, ignore_index=True)
        df.to_csv('feedback.csv', index=False)
        #check if the recent exerises are completed successfully
        df2=pd.read_csv("user.csv")
        df2["userId"]=df2["userId"].astype(str)
        df2["lastUpdate"]=pd.to_datetime(df2["lastUpdate"])
        last_update=df2.loc[df2["userId"]==userId,"lastUpdate"].reset_index(drop=True)[0]
        df["timestamp"]=pd.to_datetime(df["timestamp"])
        df=df[(df["timestamp"]>=last_update) & (df["userId"]==userId)]
        target=False
        newTargetHR=df2.loc[(df2['userId']==userId),"targetHR"].reset_index(drop=True)
        df.sort_values(by="timestamp",ascending=False,inplace=True)
        df=df.head(5)
        if df.shape[0]==5:
            if df["wuComplete"].sum()==0 and df["ex1Complete"].sum()==0 and df["ex2Complete"].sum()==0 and \
            df["ex3Complete"].sum()==0 and df["ex4Complete"].sum()==0 and df["cdComplete"].sum()==0 and \
            df["chestPain"].sum()==0 and df["shortBreath"].sum()==0 and df["jointPain"].sum()==0:
                target=True
            elif df["wuComplete"].mean()<=0.5 and df["ex1Complete"].mean()<=0.5 and df["ex2Complete"].mean()<=0.5 and \
            df["ex3Complete"].mean()<=0.5 and df["ex4Complete"].mean()<=0.5 and df["cdComplete"].mean()<=0.5 and \
            df["chestPain"].mean()<=0.5 and df["shortBreath"].mean()<=0.5 and df["jointPain"].mean()<=0.5:
                df2.loc[(df2['userId']==userId),"lastUpdate"]=datetime.datetime.now().strftime("%d/%m/%Y %H:%M:%S")
                df2.to_csv('user.csv', index=False)
                return {"newTargetHR":newTargetHR.to_dict()}, 200  # return data with 200 OK
            else:
                hr_reserve=df2.loc[(df2['userId']==userId),"hrReserve5%"].reset_index(drop=True)
                newTargetHR=df2.loc[(df2['userId']==userId),"targetHR"].reset_index(drop=True)-hr_reserve
                df2.loc[(df2['userId']==userId),"targetHR"]=newTargetHR
                df2.loc[(df2['userId']==userId),"lastUpdate"]=datetime.datetime.now().strftime("%d/%m/%Y %H:%M:%S")
                df2.to_csv('user.csv', index=False)
                return {"newTargetHR":newTargetHR.to_dict()}, 200  # return data with 200 OK
        if target:
            hr_reserve=df2.loc[(df2['userId']==userId),"hrReserve5%"].reset_index(drop=True)
            newTargetHR=df2.loc[(df2['userId']==userId),"targetHR"].reset_index(drop=True)+hr_reserve
            df2.loc[(df2['userId']==userId),"targetHR"]=newTargetHR
            df2.loc[(df2['userId']==userId),"lastUpdate"]=datetime.datetime.now().strftime("%d/%m/%Y %H:%M:%S")
            df2.to_csv('user.csv', index=False)
        return {"newTargetHR":newTargetHR.to_dict()}, 200  # return data with 200 OK

#update model                    
class Preference(Resource):
    #return list of suitable exercise
    def get(self,userId):
        #args=request.args
        df=pd.read_csv("rating.csv")
        df["userId"]=df["userId"].astype(str)
        exercise_list=df["type"].unique()
        #load user preference
        if userId not in df["userId"].unique():
            return {"preference":[exercise for exercise in exercise_list]},200
        rating=df[df['userId']==userId]
        rated_exercise=rating["type"].unique()
        #load model
        model=pickle.load(open("model.sav", 'rb'))
        rating_dict={}
        #predict missing values
        for exercise in exercise_list:
            if exercise in rated_exercise:
                rating_dict[exercise]=rating.loc[rating["type"]==exercise].rating.iloc[0]
            else:
                pred=str(model.test([[userId,exercise,None]]))
                rating_dict[exercise]=float(re.findall(r"est=(.*), details",pred)[0])
        #return sorted list
        rating_dict=dict(sorted(rating_dict.items(), key=lambda item: item[1], reverse=True)) 
        return {"preference":[key for key in rating_dict.keys()]},200
    
class Prescription(Resource):
    def get(self,userId,age,rest_heart_rate,max_heart_rate,met,st_segment,ejection_fraction,angina_level,arrhythmias,hemodynamics,cardiac_history,dysrhythmias, \
           complicated_mi, chf, ischemia, clinical_depression):
        risk="Medium"
        intensity=0
        if dysrhythmias=="Absent" and angina_level=="Absent" and hemodynamics=="Absent" and met>=7 and ejection_fraction>=50 and complicated_mi=="Absent" \
        and arrhythmias=="Absent" and chf=="Absent" and ischemia=="Absent" and clinical_depression=="Absent":
            risk="Low"
        elif arrhythmias=="Present" or st_segment>=2 or hemodynamics=="Present" or ejection_fraction<40 or cardiac_history=="Present" or dysrhythmias=="Present" \
        or complicated_mi=="Present" or chf=="Present" or ischemia=="Present" or clinical_depression=="Present":
            risk="High"
        #elif angina_level<7 or st_segment<2 or met<5 or (ejection_fraction>=40 and ejection_fraction<=49):
         #   risk="Medium"
        hr_reserve=max_heart_rate-rest_heart_rate
        if risk=="Low":
            intensity=round((0.65*hr_reserve+rest_heart_rate))
        elif risk=="Medium":
            intensity=round((0.6*hr_reserve+rest_heart_rate))
        else:
            intensity=20+rest_heart_rate
        df=pd.read_csv("user.csv")
        df["userId"]=df["userId"].astype(str)
        if userId in df['userId'].unique():
            df.loc[(df['userId']==userId),"targetHR"]=intensity
            df.loc[(df['userId']==userId),"maxHR"]=max_heart_rate
            df.loc[(df['userId']==userId),"hrReserve5%"]=0.05*hr_reserve
            df.loc[(df['userId']==userId),"lastUpdate"]=datetime.datetime.now().strftime("%d/%m/%Y %H:%M:%S")
        else:
            new_df = pd.DataFrame({"userId":userId,"targetHR":intensity,"maxHR":max_heart_rate,"duration":30,"frequency":5,"hrReserve5%":0.05*hr_reserve,"lastUpdate":datetime.datetime.now().strftime("%d/%m/%Y")},index=[0])
            df = df.append(new_df, ignore_index=True)
        df.to_csv('user.csv', index=False)
        return risk,200
    
api.add_resource(Data, '/data/<string:userId>,<string:filename>')
api.add_resource(Update, '/update/<string:userId>,<int:targetHR>,<int:maxHR>,<int:duration>,<int:frequency>')
api.add_resource(Feedback, '/feedback/<string:userId>,<string:wuComplete>,<int:wuDiff>,<int:wuEnjoy>,<string:ex1Type>,<string:ex1Complete>,<int:ex1Diff>,<int:ex1Enjoy>,'+
                 '<string:ex2Type>,<string:ex2Complete>,<int:ex2Diff>,<int:ex2Enjoy>,<string:ex3Type>,<string:ex3Complete>,<int:ex3Diff>,<int:ex3Enjoy>,'+
                 '<string:ex4Type>,<string:ex4Complete>,<int:ex4Diff>,<int:ex4Enjoy>,<string:cdComplete>,<int:cdDiff>,<int:cdEnjoy>,'+
                 '<string:chestPain>,<string:shortBreath>,<string:jointPain>')  
api.add_resource(Preference, '/preference/<string:userId>')  
api.add_resource(Prescription,'/prescription/<string:userId>,<int:age>,<int:rest_heart_rate>,<int:max_heart_rate>,<int:met>,<int:st_segment>,<int:ejection_fraction>,'+
                 '<string:angina_level>,<string:arrhythmias>,<string:hemodynamics>,<string:cardiac_history>,<string:dysrhythmias>,'+
                 '<string:complicated_mi>,<string:chf>,<string:ischemia>,<string:clinical_depression>')

if __name__ == '__main__':
    app.run()  # run our Flask app
    