import sys
sys.path.append("/home/ec2-user/.local/lib/python3.12/site-packages")
import pandas as pd
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity

data = pd.read_csv('/home/ec2-user/EwhaMoa_BE/EwhaMoa_BE/src/main/resources/ewhaclub.csv', low_memory=False)
club_name = sys.argv[1]
print(club_name)

tfidf = TfidfVectorizer()
tfidf_matrix = tfidf.fit_transform(data['content'])

cosine_sim = cosine_similarity(tfidf_matrix, tfidf_matrix)

title_to_index = dict(zip(data['group_name'], data.index))

def get_recommendations(title, cosine_sim=cosine_sim):
    idx = title_to_index[title]
    sim_scores = list(enumerate(cosine_sim[idx]))
    sim_scores = sorted(sim_scores, key=lambda x: x[1], reverse=True)
    sim_scores = sim_scores[1:4]
    club_indices = [idx[0] for idx in sim_scores]
    recommended_clubs = data['group_name'].iloc[club_indices].values
    return tuple(recommended_clubs)

recommended_clubs_tuple = get_recommendations(club_name)
print(recommended_clubs_tuple)